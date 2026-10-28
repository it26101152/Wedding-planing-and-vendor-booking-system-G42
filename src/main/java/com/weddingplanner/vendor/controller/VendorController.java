package com.weddingplanner.vendor.controller;
import com.weddingplanner.vendor.model.Vendor;
import com.weddingplanner.vendor.service.VendorService;
import com.weddingplanner.user.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/vendors") public class VendorController
{
    private final VendorService service;
    public VendorController(VendorService service)
    {
        this.service=service;
    }
    @GetMapping public String list(HttpSession session,Model model)
    {
        AuthService.requireLogin(session);
        model.addAttribute("records",service.list());
        model.addAttribute("admin", AuthService.isAdmin(session));
        return "vendors/list";
    }
    @PostMapping("/save") public String save(@ModelAttribute Vendor record,HttpSession session)
    {
        if (record.getId() != null && record.getId().isBlank()) record.setId(null);
        AuthService.requireLogin(session);
        AuthService.requireAdmin(session);
        service.save(record);
        return "redirect:/vendors";
    }
    @PostMapping("/delete") public String delete(@RequestParam String id,HttpSession session)
    {
        AuthService.requireAdmin(session);
        service.delete(id);
        return "redirect:/vendors";
    }
}
