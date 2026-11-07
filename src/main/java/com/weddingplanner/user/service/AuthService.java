package com.weddingplanner.user.service;
import com.weddingplanner.user.model.User;
import com.weddingplanner.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service public class AuthService
{
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    public AuthService(UserRepository users)
    {
        this.users=users;
    }
    public User register(String name,String email,String password)
    {
        com.weddingplanner.common.util.Validator.requireNotBlank(name, "Name");
        com.weddingplanner.common.util.Validator.requireEmail(email);
        if (name.trim().length() > 100) throw new IllegalArgumentException("Name is too long (maximum 100 characters)");
        if(password==null||password.length()<12)throw new IllegalArgumentException("Password needs at least 12 characters");
        if(users.findAll().stream().anyMatch(u->u.getEmail().equalsIgnoreCase(email.trim())))throw new IllegalArgumentException("Email already registered");
        User u=new User();
        u.setName(name.trim());
        u.setEmail(email.trim());
        u.setPasswordHash(encoder.encode(password));
        u.setRole("COUPLE");
        return users.save(u);
    }
    public User login(String email,String password)
    {
        if (email == null || password == null) throw new IllegalArgumentException("Invalid login");
        return users.findAll().stream().filter(u->u.getEmail().equalsIgnoreCase(email)
                &&encoder.matches(password,u.getPasswordHash())).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Invalid login"));
    }
    public static void requireLogin(HttpSession s)
    {
        if(s.getAttribute("uid")==null)throw new IllegalArgumentException("Please login first");
    }
    public static boolean isAdmin(HttpSession s)
    {
        return "ADMIN".equals(s.getAttribute("role"));
    }
    public static void requireAdmin(HttpSession s)
    {
        requireLogin(s);
        if(!isAdmin(s))throw new IllegalArgumentException("Admin only");
    }
    public static String userId(HttpSession s)
    {
        requireLogin(s);
        return (String)s.getAttribute("uid");
    }
}
