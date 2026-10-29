package com.weddingplanner.payment.repository;
import com.weddingplanner.payment.model.Payment;
import com.weddingplanner.common.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
@Repository public class PaymentRepository extends FileRepository<Payment>
{
    public PaymentRepository(@Value("${app.data.dir:data}") String dir)
    {
        super(dir,"payments.txt","PAY");
    }
    @Override protected Payment fromFields(String[] f)
    {
        if(f.length<4) throw new IllegalArgumentException("Invalid record");
        Payment e=new Payment();
        e.setCustomerId(f[0]);
        e.setBookingId(f[1]);
        e.setAmount(f[2]);
        e.setStatus(f[3]);
        return e;
    }
}
