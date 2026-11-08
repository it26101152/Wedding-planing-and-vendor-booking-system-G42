
package com.weddingplanner.integration;

import com.weddingplanner.user.model.User;
import com.weddingplanner.user.service.UserService;
import com.weddingplanner.vendor.model.Vendor;
import com.weddingplanner.vendor.repository.VendorRepository;
import com.weddingplanner.vendor.service.VendorService;
import com.weddingplanner.wedding.model.Wedding;
import com.weddingplanner.wedding.repository.WeddingRepository;
import com.weddingplanner.wedding.service.WeddingService;
import com.weddingplanner.booking.model.Booking;
import com.weddingplanner.booking.repository.BookingRepository;
import com.weddingplanner.booking.service.BookingService;
import com.weddingplanner.payment.model.Payment;
import com.weddingplanner.payment.repository.PaymentRepository;
import com.weddingplanner.payment.service.PaymentService;
import com.weddingplanner.guest.model.Guest;
import com.weddingplanner.guest.repository.GuestRepository;
import com.weddingplanner.guest.service.GuestService;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FiveModuleServiceTests {

    private UserService userWithRole(String role) {
        UserService users = mock(UserService.class);
        User u = new User();
        u.setRole(role);
        when(users.get("USR0002")).thenReturn(u);
        return users;
    }

    private Wedding wedding(String id) {
        Wedding w = new Wedding();
        w.setId(id);
        w.setCustomerId("USR0002");
        w.setTitle("Wedding");
        w.setVenue("Hall");
        w.setEventDate("2027-01-10");
        return w;
    }

    private Booking booking(String id) {
        Booking b = new Booking();
        b.setId(id);
        b.setCustomerId("USR0002");
        b.setVendorId("VEN0001");
        b.setEventDate("2027-01-10");
        b.setStatus("Pending");
        b.setBookingTotal("2500.00");
        return b;
    }

    private Payment payment(String id) {
        Payment p = new Payment();
        p.setId(id);
        p.setCustomerId("USR0002");
        p.setBookingId("BKG0001");
        p.setAmount("1000.00");
        p.setStatus("Paid");
        return p;
    }

    private Guest guest(String id) {
        Guest g = new Guest();
        g.setId(id);
        g.setCustomerId("USR0002");
        g.setName("Guest");
        g.setEmail("guest@example.com");
        g.setRsvp("Accepted");
        return g;
    }

    @Test
    void vendorCrudAndDeletionSafety() {
        VendorRepository repo = mock(VendorRepository.class);
        BookingRepository bookings = mock(BookingRepository.class);
        VendorService service = new VendorService(repo, bookings);

        Vendor v = new Vendor();
        v.setName("Vendor");
        v.setCategory("Photography");
        v.setDescription("Photography service");
        v.setPrice("2500.00");

        service.save(v);
        verify(repo).save(v);

        v.setPrice("-10");
        assertThrows(
                IllegalArgumentException.class,
                () -> service.save(v)
        );

        when(bookings.findAll()).thenReturn(
                List.of(booking("BKG0001"))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.delete("VEN0001")
        );

        verify(repo, never()).deleteById("VEN0001");
    }

    @Test
    void weddingExistingPromotedAdminCanUpdateButNotCreate() {
        WeddingRepository repo = mock(WeddingRepository.class);
        WeddingService service = new WeddingService(
                repo, userWithRole("ADMIN")
        );

        Wedding existing = wedding("WED0001");

        when(repo.findById("WED0001")).thenReturn(
                Optional.of(existing)
        );

        service.save(existing);
        verify(repo).save(existing);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.save(wedding(null))
        );

        Wedding changedOwner = wedding("WED0001");
        changedOwner.setCustomerId("USR0003");

        assertThrows(
                RuntimeException.class,
                () -> service.save(changedOwner)
        );
    }

    @Test
    void bookingVendorValidationAndAdminExistingRecord() {
        BookingRepository repo = mock(BookingRepository.class);
        PaymentRepository payments = mock(PaymentRepository.class);
        VendorService vendors = mock(VendorService.class);

        // FIX 1: Provide a valid vendor with a price
        Vendor vendor = new Vendor();
        vendor.setPrice("2500.00");

        when(vendors.get("VEN0001")).thenReturn(vendor);

        BookingService service = new BookingService(
                repo,
                userWithRole("ADMIN"),
                vendors,
                payments,
                "data"
        );

        Booking b = booking("BKG0001");

        when(repo.findById("BKG0001")).thenReturn(
                Optional.of(b)
        );

        service.save(b);

        verify(vendors).get("VEN0001");
        verify(repo).save(b);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.save(booking(null))
        );

        when(payments.findAll()).thenReturn(
                List.of(payment("PAY0001"))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.delete("BKG0001")
        );
    }

    @Test
    void paymentRequiresMatchingBookingAndPositiveAmount() {
        PaymentRepository repo = mock(PaymentRepository.class);
        BookingService bookings = mock(BookingService.class);

        PaymentService service = new PaymentService(
                repo,
                userWithRole("COUPLE"),
                bookings
        );

        // FIX 2: Booking helper now contains booking total
        when(bookings.get("BKG0001")).thenReturn(
                booking("BKG0001")
        );

        Payment p = payment(null);

        service.save(p);
        verify(repo).save(p);

        p.setAmount("-1");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.save(p)
        );

        p.setAmount("100");
        p.setCustomerId("USR0003");

        assertThrows(
                RuntimeException.class,
                () -> service.save(p)
        );
    }

    @Test
    void guestRsvpValidationAndAdminExistingRecord() {
        GuestRepository repo = mock(GuestRepository.class);

        GuestService service = new GuestService(
                repo,
                userWithRole("ADMIN")
        );

        Guest g = guest("GST0001");

        when(repo.findById("GST0001")).thenReturn(
                Optional.of(g)
        );

        service.save(g);
        verify(repo).save(g);

        g.setRsvp("Unknown");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.save(g)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.save(guest(null))
        );
    }
}
