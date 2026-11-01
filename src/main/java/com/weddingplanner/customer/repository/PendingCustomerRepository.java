package com.weddingplanner.customer.repository;
import com.weddingplanner.common.repository.FileRepository;
import com.weddingplanner.customer.model.PendingCustomer;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
@Repository public class PendingCustomerRepository extends FileRepository<PendingCustomer> {
 public PendingCustomerRepository(@Value("${app.data.dir:data}") String dir){super(dir,"pending_customers.txt","CUST");}
 @Override protected PendingCustomer fromFields(String[] f){if(f.length<3)throw new IllegalArgumentException("Invalid pending customer"); PendingCustomer c=new PendingCustomer();c.setName(f[0]);c.setEmail(f[1]);c.setPhone(f[2]);return c;}
}
