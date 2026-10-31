package com.weddingplanner.booking.service;
import com.weddingplanner.booking.model.Booking;
import com.weddingplanner.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.util.List;
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
        vendorService.get(ModuleValidation.required(entity.getVendorId(), "Vendor ID"));
        ModuleValidation.date(entity.getEventDate(), "Booking date");
        ModuleValidation.choice(entity.getStatus(), "Booking status", "Pending", "Confirmed", "Cancelled", "Completed");
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
