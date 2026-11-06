package com.weddingplanner.guest.repository;
import com.weddingplanner.guest.model.Guest;
import com.weddingplanner.common.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
@Repository public class GuestRepository extends FileRepository<Guest>
{
    public GuestRepository(@Value("${app.data.dir:data}") String dir)
    {
        super(dir,"guests.txt","GST");
    }
    @Override protected Guest fromFields(String[] f)
    {
        if(f.length<4) throw new IllegalArgumentException("Invalid record");
        Guest e=new Guest();
        e.setCustomerId(f[0]);
        e.setName(f[1]);
        e.setEmail(f[2]);
        e.setRsvp(f[3]);
        return e;
    }
}
