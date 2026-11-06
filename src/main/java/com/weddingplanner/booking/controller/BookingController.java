package com.weddingplanner.booking.controller;
import com.weddingplanner.booking.model.Booking;
import com.weddingplanner.vendor.service.VendorService;
import com.weddingplanner.booking.service.BookingService;
import com.weddingplanner.user.service.AuthService;
import com.weddingplanner.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/bookings") public class BookingController
{
    private final BookingService service;
    private final UserService userService;
    private final com.weddingplanner.customer.service.PendingCustomerService pendingCustomers;
    private final VendorService vendorService;
    public BookingController(BookingService service, VendorService vendorService, UserService userService, com.weddingplanner.customer.service.PendingCustomerService pendingCustomers)
    {
        this.service=service;
        this.userService=userService;
        this.pendingCustomers=pendingCustomers;
        this.vendorService=vendorService;
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
        model.addAttribute("vendors", vendorService.list());
        return "bookings/list";
    }
    @PostMapping("/save") public String save(@ModelAttribute Booking record,HttpSession session)
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
        if (record.getVendorId() == null || record.getVendorId().isBlank()) throw new IllegalArgumentException("Select a vendor");
        vendorService.get(record.getVendorId());
        service.save(record);
        return "redirect:/bookings";
    }
    @PostMapping("/delete") public String delete(@RequestParam String id,HttpSession session)
    {
        AuthService.requireLogin(session);
        if (!AuthService.isAdmin(session) && !AuthService.userId(session).equals(service.get(id).getCustomerId())) {
            throw new IllegalArgumentException("Not your record");
        }
        service.delete(id);
        return "redirect:/bookings";
    }
}
