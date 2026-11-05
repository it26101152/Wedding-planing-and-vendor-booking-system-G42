package com.weddingplanner.wedding.repository;
import com.weddingplanner.wedding.model.Wedding;
import com.weddingplanner.common.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
@Repository public class WeddingRepository extends FileRepository<Wedding>
{
    public WeddingRepository(@Value("${app.data.dir:data}") String dir)
    {
        super(dir,"weddings.txt","WED");
    }
    @Override protected Wedding fromFields(String[] f)
    {
        if(f.length<4) throw new IllegalArgumentException("Invalid record");
        Wedding e=new Wedding();
        e.setCustomerId(f[0]);
        e.setTitle(f[1]);
        e.setEventDate(f[2]);
        e.setVenue(f[3]);
        return e;
    }
}
