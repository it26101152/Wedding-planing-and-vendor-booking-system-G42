package com.weddingplanner.common.config;

import com.weddingplanner.user.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/customer")
    public String customer(HttpSession session) {
        if (session.getAttribute("uid") == null) return "redirect:/login?expired=1";
        return AuthService.isAdmin(session) ? "redirect:/admin" : "customer/dashboard";
    }

    @GetMapping("/admin")
    public String admin(HttpSession session) {
        if (session.getAttribute("uid") == null) return "redirect:/login?expired=1";
        if (!AuthService.isAdmin(session)) return "redirect:/customer";
        return "admin/dashboard";
    }
}
