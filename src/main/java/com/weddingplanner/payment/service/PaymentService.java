package com.weddingplanner.payment.service;
import com.weddingplanner.payment.model.Payment;
import com.weddingplanner.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.weddingplanner.user.service.UserService;
import com.weddingplanner.booking.service.BookingService;
import com.weddingplanner.common.util.ModuleValidation;
@Service public class PaymentService
{
    private final PaymentRepository repository;
    private final UserService userService;
    private final com.weddingplanner.customer.service.PendingCustomerService pendingCustomers;
    private final BookingService bookingService;
    private final com.weddingplanner.vendor.service.VendorService vendorService;
    public PaymentService(PaymentRepository repository, UserService userService, BookingService bookingService){this(repository,userService,bookingService,null,null);}
    @org.springframework.beans.factory.annotation.Autowired
    public PaymentService(PaymentRepository repository, UserService userService, BookingService bookingService, com.weddingplanner.customer.service.PendingCustomerService pendingCustomers, com.weddingplanner.vendor.service.VendorService vendorService)
    {
        this.repository=repository;
        this.userService=userService;
        this.pendingCustomers=pendingCustomers;
        this.bookingService=bookingService;
        this.vendorService=vendorService;
    }
    public List<Payment> list()
    {
        return repository.findAll();
    }
    public Payment get(String id)
    {
        return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Record not found"));
    }
    /** Uses the price saved with the booking; legacy bookings without a snapshot use the current price. */
    public BigDecimal bookingTotal(String bookingId) {
        var booking = bookingService.get(bookingId);
        String snapshot = booking.getBookingTotal();
        if (snapshot != null && !snapshot.isBlank())
            return new BigDecimal(snapshot).setScale(2, RoundingMode.HALF_UP);
        if (vendorService == null) throw new IllegalStateException("Legacy booking has no stored price");
        return new BigDecimal(vendorService.get(booking.getVendorId()).getPrice()).setScale(2, RoundingMode.HALF_UP);
    }
    private BigDecimal netReceived(Payment p) {
        BigDecimal amount = new BigDecimal(p.getAmount());
        if ("Paid".equals(p.getStatus()) || "Partial".equals(p.getStatus())) return amount;
        if ("Refunded".equals(p.getStatus())) {
            // Legacy Refunded records were treated as fully refunded.
            if (p.getRefundAmount() == null || p.getRefundAmount().isBlank()) return BigDecimal.ZERO;
            return amount.subtract(new BigDecimal(p.getRefundAmount()));
        }
        return BigDecimal.ZERO;
    }
    public BigDecimal paidAmount(String bookingId) {
        return repository.findAll().stream().filter(p -> bookingId.equals(p.getBookingId()))
                .map(this::netReceived).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal balance(String bookingId) {
        return bookingTotal(bookingId).subtract(paidAmount(bookingId));
    }
    public Payment save(Payment entity) {
        validateCustomer(entity);
        var booking = bookingService.get(ModuleValidation.required(entity.getBookingId(), "Booking ID"));
        if (!booking.getCustomerId().equals(entity.getCustomerId()))
            throw new IllegalArgumentException("Payment customer must match the booking customer");
        ModuleValidation.amount(entity.getAmount(), "Payment amount", false);
        ModuleValidation.choice(entity.getStatus(), "Payment status", "Pending", "Paid", "Refunded", "Unpaid", "Partial");
        BigDecimal amount = new BigDecimal(entity.getAmount());
        Payment previous = null;
        if (entity.getId() != null && !entity.getId().isBlank()) {
            previous = get(entity.getId());
            if (!previous.getBookingId().equals(entity.getBookingId()))
                throw new IllegalArgumentException("Cannot move an existing payment to another booking");
            if (!previous.getCustomerId().equals(entity.getCustomerId()))
                throw new IllegalArgumentException("Cannot change payment customer");
            if ("Refunded".equals(previous.getStatus()))
                throw new IllegalArgumentException("Refunded payments cannot be edited; create a new payment if needed");
        }
        if ("Refunded".equals(entity.getStatus())) {
            if (previous == null || !("Paid".equals(previous.getStatus()) || "Partial".equals(previous.getStatus())))
                throw new IllegalArgumentException("Only an existing Paid or Partial payment can be refunded");
            if (amount.compareTo(new BigDecimal(previous.getAmount())) != 0)
                throw new IllegalArgumentException("Original payment amount cannot change during refund");
            ModuleValidation.amount(entity.getRefundAmount(), "Refund amount", false);
            BigDecimal refund = new BigDecimal(entity.getRefundAmount());
            if (refund.compareTo(amount) > 0) throw new IllegalArgumentException("Refund exceeds original payment");
            entity.setRefundAmount(refund.setScale(2, RoundingMode.HALF_UP).toPlainString());
            entity.setRefundDate(java.time.LocalDate.now().toString());
        } else {
            entity.setRefundAmount("");
            entity.setRefundDate("");
        }
        BigDecimal otherReceived = repository.findAll().stream()
                .filter(p -> entity.getBookingId().equals(p.getBookingId()))
                .filter(p -> entity.getId() == null || !entity.getId().equals(p.getId()))
                .map(this::netReceived).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (otherReceived.add(netReceived(entity)).compareTo(bookingTotal(entity.getBookingId())) > 0)
            throw new IllegalArgumentException("Payment exceeds the remaining booking balance");
        return repository.save(entity);
    }
    /** Preserve existing records after a customer is promoted to ADMIN. */
    private void validateCustomer(Payment entity) {
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
        return repository.deleteById(id);
    }
}
