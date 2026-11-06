package com.weddingplanner.user.service;
import com.weddingplanner.user.model.User;
import com.weddingplanner.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class UserService
{
    private final UserRepository repository;
    public UserService(UserRepository repository)
    {
        this.repository=repository;
    }
    public List<User> list()
    {
        return repository.findAll();
    }
    public User get(String id)
    {
        return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Record not found"));
    }
    public User save(User entity)
    {
        return repository.save(entity);
    }
    /** The oldest ADMIN record is the permanent main administrator. */
    public String mainAdminId() {
        return repository.findAll().stream()
            .filter(u -> "ADMIN".equals(u.getRole()))
            .min(java.util.Comparator.comparing(User::getCreatedAt))
            .map(User::getId).orElseThrow(() -> new IllegalStateException("No main admin exists"));
    }
    public boolean isMainAdmin(String id) {
        return id != null && id.equals(mainAdminId());
    }
    public void validateEmail(String email, String exceptId) {
        if (email == null || !email.trim().matches("^[^@ ]+@[^@ ]+\\.[^@ ]+$"))
            throw new IllegalArgumentException("Enter a valid email address");
        if (repository.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email.trim())
                && !u.getId().equals(exceptId)))
            throw new IllegalArgumentException("Email already registered");
    }
    public boolean delete(String id)
    {
        return repository.deleteById(id);
    }
}
