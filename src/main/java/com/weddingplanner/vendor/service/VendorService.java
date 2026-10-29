package com.weddingplanner.vendor.service;
import com.weddingplanner.vendor.model.Vendor;
import com.weddingplanner.vendor.repository.VendorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import com.weddingplanner.common.util.ModuleValidation;
@Service public class VendorService
{
    private final VendorRepository repository;
    private final com.weddingplanner.booking.repository.BookingRepository bookingRepository;
    public VendorService(VendorRepository repository, com.weddingplanner.booking.repository.BookingRepository bookingRepository)
    {
        this.repository=repository;
        this.bookingRepository=bookingRepository;
    }
    public List<Vendor> list()
    {
        return repository.findAll();
    }
    public Vendor get(String id)
    {
        return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Record not found"));
    }
    public Vendor save(Vendor entity)
    {
        ModuleValidation.required(entity.getName(), "Vendor name");
        ModuleValidation.required(entity.getCategory(), "Vendor category");
        ModuleValidation.required(entity.getDescription(), "Vendor description");
        ModuleValidation.amount(entity.getPrice(), "Vendor price", true);
        return repository.save(entity);
    }
    public boolean delete(String id)
    {
        if (bookingRepository.findAll().stream().anyMatch(b -> id.equals(b.getVendorId())))
            throw new IllegalArgumentException("Cannot delete this vendor because it is used by existing bookings. Remove or reassign those bookings first.");
        return repository.deleteById(id);
    }
}
