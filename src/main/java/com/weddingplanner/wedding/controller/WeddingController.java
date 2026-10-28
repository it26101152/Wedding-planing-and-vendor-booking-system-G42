package com.weddingplanner.wedding.controller;
import com.weddingplanner.wedding.model.Wedding;
import com.weddingplanner.wedding.service.WeddingService;
import com.weddingplanner.user.service.AuthService;
import com.weddingplanner.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/weddings") public class WeddingController
{
    private final WeddingService service;
    private final UserService userService;
    private final com.weddingplanner.customer.service.PendingCustomerService pendingCustomers;
    public WeddingController(WeddingService service, UserService userService, com.weddingplanner.customer.service.PendingCustomerService pendingCustomers)
    {
        this.service=service;
        this.userService=userService;
        this.pendingCustomers=pendingCustomers;
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
        return "weddings/list";
    }
    @PostMapping("/save") public String save(@ModelAttribute Wedding record, @RequestParam(required=false) String newCustomerName, @RequestParam(required=false) String newCustomerEmail, @RequestParam(required=false) String newCustomerPhone, HttpSession session)
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
        if (AuthService.isAdmin(session) && "__new__".equals(record.getCustomerId())) {
            if (record.getId()!=null) throw new IllegalArgumentException("Cannot change existing plan to a new customer");
            com.weddingplanner.common.util.ModuleValidation.required(record.getTitle(), "Wedding title");
            com.weddingplanner.common.util.ModuleValidation.required(record.getVenue(), "Wedding venue");
            com.weddingplanner.common.util.ModuleValidation.date(record.getEventDate(), "Wedding date");
            var pending = pendingCustomers.create(newCustomerName,newCustomerEmail,newCustomerPhone);
            record.setCustomerId(pending.getId());
        }
        service.save(record);
        return "redirect:/weddings";
    }
    @PostMapping("/delete") public String delete(@RequestParam String id,HttpSession session)
    {
        AuthService.requireLogin(session);
        if (!AuthService.isAdmin(session) && !AuthService.userId(session).equals(service.get(id).getCustomerId())) {
            throw new IllegalArgumentException("Not your record");
        }
        service.delete(id);
        return "redirect:/weddings";
    }
}
