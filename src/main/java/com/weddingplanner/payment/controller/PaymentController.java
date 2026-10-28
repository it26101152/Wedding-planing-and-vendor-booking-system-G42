package com.weddingplanner.payment.controller;
import com.weddingplanner.payment.model.Payment;
import com.weddingplanner.booking.service.BookingService;
import com.weddingplanner.payment.service.PaymentService;
import com.weddingplanner.user.service.AuthService;
import com.weddingplanner.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/payments") public class PaymentController
{
    private final PaymentService service;
    private final UserService userService;
    private final com.weddingplanner.customer.service.PendingCustomerService pendingCustomers;
    private final BookingService bookingService;
    public PaymentController(PaymentService service, BookingService bookingService, UserService userService, com.weddingplanner.customer.service.PendingCustomerService pendingCustomers)
    {
        this.service=service;
        this.userService=userService;
        this.pendingCustomers=pendingCustomers;
        this.bookingService=bookingService;
    }
    @GetMapping public String list(HttpSession session,Model model)
    {
        AuthService.requireLogin(session);
        model.addAttribute("records",AuthService.isAdmin(session)?service.list():service.list()
                .stream().filter(r->AuthService.userId(session).equals(r.getCustomerId()))
                .toList());
        model.addAttribute("admin", AuthService.isAdmin(session));
        if (AuthService.isAdmin(session)) {
            model.addAttribute("customers", userService.list().stream().filter(u -> "COUPLE".equals(u.getRole())).toList());
            model.addAttribute("pendingCustomers", pendingCustomers.list());
        }
        model.addAttribute("bookings", AuthService.isAdmin(session) ? bookingService.list() : bookingService.list().stream()
                .filter(r -> AuthService.userId(session).equals(r.getCustomerId())).toList());
        return "payments/list";
    }
    @PostMapping("/save") public String save(@ModelAttribute Payment record,HttpSession session)
    {
        if (record.getId() != null && record.getId().isBlank()) record.setId(null);
        AuthService.requireLogin(session);
        if (!AuthService.isAdmin(session) && record.getId() != null && !record.getId().isBlank()) {
            var existing = service.get(record.getId());
            if (!AuthService.userId(session).equals(existing.getCustomerId())) {
                throw new IllegalArgumentException("Not your record");
            }
        }
        if (!AuthService.isAdmin(session)) {
            record.setCustomerId(AuthService.userId(session));
        }
        if (record.getBookingId() == null || record.getBookingId().isBlank()) throw new IllegalArgumentException("Select a booking");
        var booking = bookingService.get(record.getBookingId());
        if (!AuthService.isAdmin(session) && !AuthService.userId(session).equals(booking.getCustomerId())) {
            throw new IllegalArgumentException("Not your booking");
        }
        if (!booking.getCustomerId().equals(record.getCustomerId()))
            throw new IllegalArgumentException("Payment customer must match the selected booking");
        service.save(record);
        return "redirect:/payments";
    }
    @PostMapping("/delete") public String delete(@RequestParam String id,HttpSession session)
    {
        AuthService.requireLogin(session);
        if (!AuthService.isAdmin(session) && !AuthService.userId(session).equals(service.get(id).getCustomerId())) {
            throw new IllegalArgumentException("Not your record");
        }
        service.delete(id);
        return "redirect:/payments";
    }
}
