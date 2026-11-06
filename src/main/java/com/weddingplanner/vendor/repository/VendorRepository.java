package com.weddingplanner.vendor.repository;
import com.weddingplanner.vendor.model.Vendor;
import com.weddingplanner.common.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
@Repository public class VendorRepository extends FileRepository<Vendor>
{
    public VendorRepository(@Value("${app.data.dir:data}") String dir)
    {
        super(dir,"vendors.txt","VEN");
    }
    @Override protected Vendor fromFields(String[] f)
    {
        if(f.length<4) throw new IllegalArgumentException("Invalid record");
        Vendor e=new Vendor();
        e.setName(f[0]);
        e.setCategory(f[1]);
        e.setPrice(f[2]);
        e.setDescription(f[3]);
        return e;
    }
}
