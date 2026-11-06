package com.weddingplanner.guest.service;
import com.weddingplanner.guest.model.Guest;
import com.weddingplanner.guest.repository.GuestRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.weddingplanner.user.service.UserService;
import com.weddingplanner.common.util.ModuleValidation;
@Service public class GuestService
{
    private final GuestRepository repository;
    private final UserService userService;
    private final com.weddingplanner.customer.service.PendingCustomerService pendingCustomers;
    public GuestService(GuestRepository repository, UserService userService){this(repository,userService,null);}
    @org.springframework.beans.factory.annotation.Autowired
    public GuestService(GuestRepository repository, UserService userService, com.weddingplanner.customer.service.PendingCustomerService pendingCustomers)
    {
        this.repository=repository;
        this.userService=userService;
        this.pendingCustomers=pendingCustomers;
    }
    public List<Guest> list()
    {
        return repository.findAll();
    }
    public Guest get(String id)
    {
        return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Record not found"));
    }
    public Guest save(Guest entity)
    {
        validateCustomer(entity);
        ModuleValidation.required(entity.getName(), "Guest name");
        ModuleValidation.email(entity.getEmail());
        ModuleValidation.choice(entity.getRsvp(), "RSVP status", "Pending", "Accepted", "Declined");
        return repository.save(entity);
    }
    /** Preserve existing records after a customer is promoted to ADMIN. */
    private void validateCustomer(Guest entity) {
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
