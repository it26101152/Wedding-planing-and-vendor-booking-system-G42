package com.weddingplanner.payment.service;
import com.weddingplanner.payment.model.Payment;
import com.weddingplanner.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.weddingplanner.user.service.UserService;
import com.weddingplanner.booking.service.BookingService;
import com.weddingplanner.common.util.ModuleValidation;
@Service public class PaymentService
{
    private final PaymentRepository repository;
    private final UserService userService;
    private final com.weddingplanner.customer.service.PendingCustomerService pendingCustomers;
    private final BookingService bookingService;
    public PaymentService(PaymentRepository repository, UserService userService, BookingService bookingService){this(repository,userService,bookingService,null);}
    @org.springframework.beans.factory.annotation.Autowired
    public PaymentService(PaymentRepository repository, UserService userService, BookingService bookingService, com.weddingplanner.customer.service.PendingCustomerService pendingCustomers)
    {
        this.repository=repository;
        this.userService=userService;
        this.pendingCustomers=pendingCustomers;
        this.bookingService=bookingService;
    }
    public List<Payment> list()
    {
        return repository.findAll();
    }
    public Payment get(String id)
    {
        return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Record not found"));
    }
    public Payment save(Payment entity)
    {
        validateCustomer(entity);
        var booking = bookingService.get(ModuleValidation.required(entity.getBookingId(), "Booking ID"));
        if (!booking.getCustomerId().equals(entity.getCustomerId()))
            throw new IllegalArgumentException("Payment customer must match the booking customer");
        ModuleValidation.amount(entity.getAmount(), "Payment amount", false);
        ModuleValidation.choice(entity.getStatus(), "Payment status", "Pending", "Paid", "Refunded", "Unpaid", "Partial");
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