package com.weddingplanner.booking.repository;
import com.weddingplanner.booking.model.Booking;
import com.weddingplanner.common.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
@Repository public class BookingRepository extends FileRepository<Booking>
{
    public BookingRepository(@Value("${app.data.dir:data}") String dir)
    {
        super(dir,"bookings.txt","BKG");
    }
    @Override protected Booking fromFields(String[] f)
    {
        if(f.length<4) throw new IllegalArgumentException("Invalid record");
        Booking e=new Booking();
        e.setCustomerId(f[0]);
        e.setVendorId(f[1]);
        e.setEventDate(f[2]);
        e.setStatus(f[3]);
        return e;
    }
}
