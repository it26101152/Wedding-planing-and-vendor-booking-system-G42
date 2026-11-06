package com.weddingplanner.wedding.service;
import com.weddingplanner.wedding.model.Wedding;
import com.weddingplanner.wedding.repository.WeddingRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.weddingplanner.user.service.UserService;
import com.weddingplanner.common.util.ModuleValidation;
@Service public class WeddingService
{
    private final WeddingRepository repository;
    private final UserService userService;
    private final com.weddingplanner.customer.service.PendingCustomerService pendingCustomers;
    public WeddingService(WeddingRepository repository, UserService userService){this(repository,userService,null);}
    @org.springframework.beans.factory.annotation.Autowired
    public WeddingService(WeddingRepository repository, UserService userService, com.weddingplanner.customer.service.PendingCustomerService pendingCustomers)
    {
        this.repository=repository;
        this.userService=userService;
        this.pendingCustomers=pendingCustomers;
    }
    public List<Wedding> list()
    {
        return repository.findAll();
    }
    public Wedding get(String id)
    {
        return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Record not found"));
    }
    public Wedding save(Wedding entity)
    {
        ModuleValidation.required(entity.getTitle(), "Wedding title");
        ModuleValidation.required(entity.getVenue(), "Wedding venue");
        ModuleValidation.date(entity.getEventDate(), "Wedding date");
        validateCustomer(entity);
        return repository.save(entity);
    }
    /** Preserve existing records after a customer is promoted to ADMIN. */
    private void validateCustomer(Wedding entity) {
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
