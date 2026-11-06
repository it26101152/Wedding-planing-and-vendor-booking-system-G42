package com.weddingplanner.user.controller;
import com.weddingplanner.user.model.User;
import com.weddingplanner.user.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller public class AuthController
{
    private final AuthService auth;
    public AuthController(AuthService auth)
    {
        this.auth=auth;
    }
    @GetMapping("/login") public String loginPage()
    {
        return "auth/login";
    }
    @GetMapping("/register") public String registerPage()
    {
        return "auth/register";
    }
    @PostMapping("/register") public String register(@RequestParam String name,@RequestParam String email,@RequestParam String password,HttpSession session, Model model)
    {
        try {
            User u=auth.register(name,email,password);
            session.setAttribute("uid",u.getId());
            session.setAttribute("role",u.getRole());
            return "redirect:/customer";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registerError", ex.getMessage());
            model.addAttribute("enteredName", name);
            model.addAttribute("enteredEmail", email);
            return "auth/register";
        }
    }
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
    HttpSession session, Model model)
    {
        try
        {
            User u = auth.login(email, password);
            session.setAttribute("uid", u.getId());
            session.setAttribute("role", u.getRole());
            return "ADMIN".equals(u.getRole()) ? "redirect:/admin" : "redirect:/customer";
        }
        catch (IllegalArgumentException ex)
        {
            model.addAttribute("loginError", "Invalid email or password. Please try again.");
            return "auth/login";
        }
    }
    @PostMapping("/logout") public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/login";
    }
}
