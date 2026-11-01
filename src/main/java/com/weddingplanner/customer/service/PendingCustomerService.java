package com.weddingplanner.customer.service;
import com.weddingplanner.customer.repository.PendingCustomerRepository;
import com.weddingplanner.customer.model.PendingCustomer;
import com.weddingplanner.user.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class PendingCustomerService {
 private final PendingCustomerRepository repo; private final UserService users;
 public PendingCustomerService(PendingCustomerRepository repo,UserService users){this.repo=repo;this.users=users;}
 public List<PendingCustomer> list(){return repo.findAll();}
 public PendingCustomer get(String id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Customer reference not found"));}
 public boolean isPending(String id){return id!=null && id.startsWith("CUST") && repo.findById(id).isPresent();}
 public synchronized PendingCustomer create(String name,String email,String phone){
   if(name==null||name.isBlank()||name.trim().length()>100)throw new IllegalArgumentException("Enter a valid customer name");
   if(email==null||!email.trim().matches("^[^@ ]+@[^@ ]+\\.[^@ ]+$"))throw new IllegalArgumentException("Enter a valid customer email");
   if(phone==null||phone.isBlank()||phone.length()>40)throw new IllegalArgumentException("Enter a customer phone number");
   String normalized=email.trim();
   if(repo.findAll().stream().anyMatch(c->c.getEmail().equalsIgnoreCase(normalized)))throw new IllegalArgumentException("This email already has a pending customer reference; select it from the list");
   if(users.list().stream().anyMatch(u->u.getEmail().equalsIgnoreCase(normalized)))throw new IllegalArgumentException("This email is already registered; select the existing customer");
   PendingCustomer c=new PendingCustomer();c.setName(name.trim());c.setEmail(normalized);c.setPhone(phone.trim());return repo.save(c);
 }
}
