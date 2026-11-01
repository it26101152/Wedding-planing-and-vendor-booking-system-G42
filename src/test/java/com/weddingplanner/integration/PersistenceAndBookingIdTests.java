package com.weddingplanner.integration;

import com.weddingplanner.booking.model.Booking;
import com.weddingplanner.booking.repository.BookingRepository;
import com.weddingplanner.vendor.model.Vendor;
import com.weddingplanner.vendor.repository.VendorRepository;
import com.weddingplanner.wedding.model.Wedding;
import com.weddingplanner.wedding.repository.WeddingRepository;
import com.weddingplanner.payment.model.Payment;
import com.weddingplanner.payment.repository.PaymentRepository;
import com.weddingplanner.guest.model.Guest;
import com.weddingplanner.guest.repository.GuestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class PersistenceAndBookingIdTests {
    @TempDir Path data;
    private Booking booking() {
        Booking b = new Booking(); b.setCustomerId("USR0001"); b.setVendorId("VEN0001");
        b.setEventDate("2027-05-20"); b.setStatus("Pending"); return b;
    }
    @Test void bookingIdsNeverReusedAfterDeleteOrRestart() {
        BookingRepository r = new BookingRepository(data.toString());
        String first = r.save(booking()).getId();
        String second = r.save(booking()).getId();
        assertNotEquals(first, second);
        assertTrue(r.deleteById(second));
        BookingRepository restarted = new BookingRepository(data.toString());
        assertEquals(first, restarted.findAll().get(0).getId());
        String third = restarted.save(booking()).getId();
        assertNotEquals(second, third);
        assertEquals("BKG0003", third);
        assertEquals(2, new BookingRepository(data.toString()).findAll().size());
    }
    @Test void fiveModulesSurviveRepositoryRestart() {
        String dir = data.toString();
        Vendor v = new Vendor(); v.setName("Photo"); v.setCategory("Photography");
        v.setPrice("1500"); v.setDescription("Studio");
        String vid = new VendorRepository(dir).save(v).getId();
        Wedding w = new Wedding(); w.setCustomerId("USR0001"); w.setTitle("Wedding");
        w.setVenue("Hall"); w.setEventDate("2027-05-20");
        String wid = new WeddingRepository(dir).save(w).getId();
        Booking b = booking(); b.setVendorId(vid);
        String bid = new BookingRepository(dir).save(b).getId();
        Payment p = new Payment(); p.setCustomerId("USR0001"); p.setBookingId(bid);
        p.setAmount("500"); p.setStatus("Paid");
        String pid = new PaymentRepository(dir).save(p).getId();
        Guest g = new Guest(); g.setCustomerId("USR0001"); g.setName("Alice");
        g.setEmail("alice@example.com"); g.setRsvp("Accepted");
        String gid = new GuestRepository(dir).save(g).getId();
        assertEquals("Photo", new VendorRepository(dir).findById(vid).orElseThrow().getName());
        assertEquals("Hall", new WeddingRepository(dir).findById(wid).orElseThrow().getVenue());
        assertEquals(vid, new BookingRepository(dir).findById(bid).orElseThrow().getVendorId());
        assertEquals(bid, new PaymentRepository(dir).findById(pid).orElseThrow().getBookingId());
        assertEquals("Accepted", new GuestRepository(dir).findById(gid).orElseThrow().getRsvp());
    }
}
