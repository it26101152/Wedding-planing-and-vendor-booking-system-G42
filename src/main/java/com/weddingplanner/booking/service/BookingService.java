package com.weddingplanner.booking.service;
import com.weddingplanner.booking.model.Booking;
import com.weddingplanner.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.weddingplanner.user.service.UserService;
import com.weddingplanner.vendor.service.VendorService;
import com.weddingplanner.common.util.ModuleValidation;
@Service public class BookingService
{
    private final BookingRepository repository;
    private final com.weddingplanner.payment.repository.PaymentRepository paymentRepository;
    private final java.nio.file.Path confirmationFile;
    private final UserService userService;
    private final com.weddingplanner.customer.service.PendingCustomerService pendingCustomers;
    private final VendorService vendorService;
    public BookingService(BookingRepository repository, UserService userService, VendorService vendorService, com.weddingplanner.payment.repository.PaymentRepository paymentRepository, String dataDir){this(repository,userService,vendorService,paymentRepository,null,dataDir);}
    @org.springframework.beans.factory.annotation.Autowired
    public BookingService(BookingRepository repository, UserService userService, VendorService vendorService, com.weddingplanner.payment.repository.PaymentRepository paymentRepository, com.weddingplanner.customer.service.PendingCustomerService pendingCustomers,
                          @org.springframework.beans.factory.annotation.Value("${app.data.dir:data}") String dataDir)
    {
        this.repository=repository;
        this.paymentRepository=paymentRepository;
        this.confirmationFile=java.nio.file.Paths.get(dataDir, "confirmations.txt");
        this.userService=userService;
        this.pendingCustomers=pendingCustomers;
        this.vendorService=vendorService;
    }
    public List<Booking> list()
    {
        return repository.findAll();
    }
    public Booking get(String id)
    {
        return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Record not found"));
    }
    public Booking save(Booking entity)
    {
        validateCustomer(entity);
        var vendor = vendorService.get(ModuleValidation.required(entity.getVendorId(), "Vendor ID"));
        ModuleValidation.date(entity.getEventDate(), "Booking date");
        ModuleValidation.choice(entity.getStatus(), "Booking status", "Pending", "Confirmed", "Cancelled", "Completed");
        // A vendor can have only one active booking per day. Cancelled bookings do not block.
        if (!"Cancelled".equals(entity.getStatus())) {
            boolean conflict = repository.findAll().stream().anyMatch(existing ->
                !java.util.Objects.equals(existing.getId(), entity.getId())
                && existing.getVendorId().equals(entity.getVendorId())
                && existing.getEventDate().equals(entity.getEventDate())
                && !"Cancelled".equals(existing.getStatus()));
            if (conflict) throw new IllegalArgumentException("Vendor is already booked for this date");
        }
        // Never accept a total supplied by the browser; snapshot the vendor price on CREATE.
        if (entity.getId() == null || entity.getId().isBlank()) {
            BigDecimal total = new BigDecimal(vendor.getPrice()).setScale(2, RoundingMode.HALF_UP);
            if (total.signum() <= 0) throw new IllegalArgumentException("Vendor price must be positive");
            entity.setBookingTotal(total.toPlainString());
        } else {
            Booking existing = get(entity.getId());
            if (paymentRepository.findAll().stream().anyMatch(p -> entity.getId().equals(p.getBookingId()))
                && (!existing.getVendorId().equals(entity.getVendorId())
                    || !existing.getCustomerId().equals(entity.getCustomerId())))
                throw new IllegalArgumentException("Cannot change booking vendor/customer after payments exist");
            entity.setBookingTotal(existing.getBookingTotal());
            // Migrate old bookings to a price snapshot on first update, if no stored snapshot exists.
            if (entity.getBookingTotal().isBlank())
                entity.setBookingTotal(new BigDecimal(vendor.getPrice()).setScale(2, RoundingMode.HALF_UP).toPlainString());
        }
        return repository.save(entity);
    }
    /** Preserve existing records after a customer is promoted to ADMIN. */
    private void validateCustomer(Booking entity) {
        String customerId = ModuleValidation.required(entity.getCustomerId(), "Customer ID");
        if (pendingCustomers != null && pendingCustomers.isPending(customerId)) return;
        String role = userService.get(customerId).getRole();
        if ("COUPLE".equals(role)) return;
        if ("ADMIN".equals(role) && entity.getId() != null) {
            var existing = repository.findById(entity.getId());
            if (existing.isPresent() && customerId.equals(existing.get().getCustomerId())) return;
        }
        throw new IllegalArgumentException(
            "Select a registered customer account. Promoted admins may update only their existing records.");
    }
    public boolean delete(String id)
    {
        if (paymentRepository.findAll().stream().anyMatch(p -> id.equals(p.getBookingId())))
            throw new IllegalArgumentException("Cannot delete this booking because payments reference it. Remove those payments first.");
        if (java.nio.file.Files.exists(confirmationFile)) {
            try {
                boolean confirmed = java.nio.file.Files.readAllLines(confirmationFile).stream()
                    .map(line -> line.split("\\|", -1))
                    .anyMatch(parts -> parts.length >= 2 && id.equals(parts[1]));
                if (confirmed) throw new IllegalArgumentException("Cannot delete a confirmed booking.");
            } catch (java.io.IOException ex) {
                throw new IllegalStateException("Unable to verify booking confirmations; deletion cancelled.", ex);
            }
        }
        return repository.deleteById(id);
    }
}
