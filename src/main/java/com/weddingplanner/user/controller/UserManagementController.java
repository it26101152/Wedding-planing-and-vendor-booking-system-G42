package com.weddingplanner.user.controller;

import com.weddingplanner.user.model.User;
import com.weddingplanner.user.service.AuthService;
import com.weddingplanner.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserManagementController {
    private final UserService service;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserManagementController(UserService service) {
        this.service = service;
    }

    private boolean mainAdmin(HttpSession session) {
        AuthService.requireAdmin(session);
        return service.isMainAdmin(AuthService.userId(session));
    }

    private void requireMainAdmin(HttpSession session) {
        if (!mainAdmin(session)) {
            throw new IllegalArgumentException("Only the main administrator can manage admin accounts");
        }
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        AuthService.requireAdmin(session);
        model.addAttribute("records", service.list());
        model.addAttribute("mainAdmin", mainAdmin(session));
        model.addAttribute("mainAdminId", service.mainAdminId());
        model.addAttribute("currentUserId", AuthService.userId(session));
        return "users/list";
    }

    @PostMapping("/admin/promote")
    public String promote(@RequestParam String id, HttpSession session) {
        requireMainAdmin(session);
        User target = service.get(id);
        if (!"COUPLE".equals(target.getRole()))
            throw new IllegalArgumentException("Only registered customer accounts can be promoted");
        target.setRole("ADMIN");
        service.save(target);
        return "redirect:/users";
    }

    @PostMapping("/admin/demote")
    public String demote(@RequestParam String id, HttpSession session) {
        requireMainAdmin(session);
        if (service.isMainAdmin(id))
            throw new IllegalArgumentException("The main administrator cannot be demoted");
        if (id.equals(AuthService.userId(session)))
            throw new IllegalArgumentException("You cannot remove your own administrator access");
        User target = service.get(id);
        if (!"ADMIN".equals(target.getRole()))
            throw new IllegalArgumentException("This account is not an administrator");
        target.setRole("COUPLE");
        service.save(target);
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String update(@RequestParam String id, @RequestParam String name,
                         @RequestParam String email,
                         @RequestParam(required = false) String password,
                         HttpSession session) {
        AuthService.requireAdmin(session);
        User user = service.get(id);
        String actorId = AuthService.userId(session);
        if (!"COUPLE".equals(user.getRole()))
            throw new IllegalArgumentException("Admin accounts cannot be updated from User Management");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        service.validateEmail(email, id);
        user.setName(name.trim());
        user.setEmail(email.trim());
        if (password != null && !password.isBlank())
            throw new IllegalArgumentException("Passwords cannot be changed from User Management");
        service.save(user);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String id, HttpSession session) {
        AuthService.requireAdmin(session);
        User target = service.get(id);
        if (!"COUPLE".equals(target.getRole()))
            throw new IllegalArgumentException("Administrator accounts cannot be deleted");
        if (id.equals(AuthService.userId(session)))
            throw new IllegalArgumentException("You cannot delete your own account");
        service.delete(id);
        return "redirect:/users";
    }
}
