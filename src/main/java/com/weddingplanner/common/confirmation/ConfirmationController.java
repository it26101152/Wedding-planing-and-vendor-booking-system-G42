package com.weddingplanner.common.confirmation;

import com.weddingplanner.booking.model.Booking;
import com.weddingplanner.booking.service.BookingService;
import com.weddingplanner.guest.service.GuestService;
import com.weddingplanner.payment.service.PaymentService;
import com.weddingplanner.user.service.AuthService;
import com.weddingplanner.vendor.service.VendorService;
import com.weddingplanner.wedding.service.WeddingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/customer/confirmation")
public class ConfirmationController {
    private final WeddingService weddings;
    private final BookingService bookings;
    private final PaymentService payments;
    private final GuestService guests;
    private final VendorService vendors;
    private final Path file;

    public ConfirmationController(WeddingService weddings, BookingService bookings,
                                  PaymentService payments, GuestService guests, VendorService vendors,
                                  @org.springframework.beans.factory.annotation.Value("${app.data.dir:data}") String dataDir) {
        this.weddings = weddings;
        this.bookings = bookings;
        this.payments = payments;
        this.guests = guests;
        this.vendors = vendors;
        this.file = Paths.get(dataDir, "confirmations.txt");
    }

    @GetMapping
    public ResponseEntity<?> review(HttpSession session) {
        if (!customer(session)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Customer login required"));
        String uid = AuthService.userId(session);
        List<Map<String, String>> options = new ArrayList<>();
        for (Booking b : bookings.list()) {
            if (!uid.equals(b.getCustomerId())) continue;
            boolean vendorExists = vendors.list().stream().anyMatch(v -> v.getId().equals(b.getVendorId()));
            boolean weddingExists = weddings.list().stream().anyMatch(w -> uid.equals(w.getCustomerId()) && w.getEventDate().equals(b.getEventDate()) && !w.getTitle().isBlank());
            boolean paymentExists = payments.list().stream().anyMatch(p -> uid.equals(p.getCustomerId()) && b.getId().equals(p.getBookingId()) && !p.getAmount().isBlank());
            boolean guestExists = guests.list().stream().anyMatch(g -> uid.equals(g.getCustomerId()) && !g.getName().isBlank());
            boolean validStatus = !"Cancelled".equalsIgnoreCase(b.getStatus());
            boolean ready = vendorExists && weddingExists && paymentExists && guestExists && validStatus && !b.getEventDate().isBlank();
            Map<String, String> option = new LinkedHashMap<>();
            option.put("id", b.getId());
            option.put("date", b.getEventDate());
            option.put("vendorId", b.getVendorId());
            option.put("ready", String.valueOf(ready));
            List<String> missing = new ArrayList<>();
            if (!weddingExists) missing.add("Wedding plan missing for event date " + b.getEventDate());
            if (!vendorExists) missing.add("Vendor " + b.getVendorId() + " not found. Select an existing vendor and save this booking.");
            if (!paymentExists) missing.add("No payment linked to booking " + b.getId() + ". Go to Payments and select this exact booking ID, then save.");
            if (!guestExists) missing.add("No guest saved. Add a guest in step 4.");
            if (b.getEventDate().isBlank()) missing.add("Booking event date is empty.");
            if (!validStatus) missing.add("Cancelled bookings cannot be confirmed.");
            option.put("details", missing.isEmpty() ? "All required records are linked correctly." : String.join(" | ", missing));
            option.put("confirmed", String.valueOf(isConfirmed(uid, b.getId())));
            options.add(option);
        }
        return ResponseEntity.ok(Map.of("bookings", options));
    }

    @PostMapping
    public synchronized ResponseEntity<?> confirm(@RequestParam String bookingId, HttpSession session) {
        if (!customer(session)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Customer login required"));
        String uid = AuthService.userId(session);
        Booking booking = bookings.list().stream().filter(b -> bookingId.equals(b.getId()) && uid.equals(b.getCustomerId())).findFirst().orElse(null);
        if (booking == null) return ResponseEntity.badRequest().body(Map.of("error", "Booking not found for this customer"));
        boolean vendorExists = vendors.list().stream().anyMatch(v -> v.getId().equals(booking.getVendorId()));
        boolean weddingExists = weddings.list().stream().anyMatch(w -> uid.equals(w.getCustomerId()) && w.getEventDate().equals(booking.getEventDate()) && !w.getTitle().isBlank());
        boolean paymentExists = payments.list().stream().anyMatch(p -> uid.equals(p.getCustomerId()) && bookingId.equals(p.getBookingId()) && !p.getAmount().isBlank());
        boolean guestExists = guests.list().stream().anyMatch(g -> uid.equals(g.getCustomerId()) && !g.getName().isBlank());
        if (!vendorExists || !weddingExists || !paymentExists || !guestExists || booking.getEventDate().isBlank() || "Cancelled".equalsIgnoreCase(booking.getStatus())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Check the booking ID and linked records. Your payment must reference this exact booking ID; the vendor must exist, and the wedding plan must have the same event date."));
        }
        if (isConfirmed(uid, bookingId)) return ResponseEntity.ok(Map.of("message", "This booking is already confirmed."));
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, uid + "|" + bookingId + "|" + Instant.now() + System.lineSeparator(),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return ResponseEntity.ok(Map.of("message", "Booking confirmation saved successfully."));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Unable to save confirmation."));
        }
    }

    private boolean customer(HttpSession session) {
        return session.getAttribute("uid") != null && !AuthService.isAdmin(session);
    }

    private boolean isConfirmed(String uid, String bookingId) {
        if (!Files.exists(file)) return false;
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8).stream()
                    .anyMatch(line -> line.startsWith(uid + "|" + bookingId + "|"));
        } catch (IOException e) {
            return false;
        }
    }
}
