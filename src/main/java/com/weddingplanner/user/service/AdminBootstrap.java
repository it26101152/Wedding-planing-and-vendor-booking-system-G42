package com.weddingplanner.user.service;
import com.weddingplanner.user.model.User;
import com.weddingplanner.user.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
/** Creates the first admin only when explicitly configured via environment variables. */
@Component
public class AdminBootstrap implements ApplicationRunner
{
    private final UserRepository users;
    public AdminBootstrap(UserRepository users)
    {
        this.users = users;
    }
    @Override public void run(ApplicationArguments args)
    {
        String email = System.getenv("WEDDING_ADMIN_EMAIL");
        String password = System.getenv("WEDDING_ADMIN_PASSWORD");
        if (email == null || email.isBlank() || password == null || password.isBlank()) return;
        if (password.length() < 12) throw new IllegalArgumentException("WEDDING_ADMIN_PASSWORD must be at least 12 characters");
        if (users.findAll().stream().anyMatch(u -> "ADMIN".equals(u.getRole()))) return;
        if (users.findAll().stream().anyMatch(u -> email.equalsIgnoreCase(u.getEmail())))
        throw new IllegalArgumentException("Admin email is already registered as customer");
        User admin = new User();
        admin.setName("Administrator");
        admin.setEmail(email.trim());
        admin.setRole("ADMIN");
        admin.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        users.save(admin);
    }
}
