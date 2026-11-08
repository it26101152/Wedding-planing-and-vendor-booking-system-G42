package com.weddingplanner.user.controller;

import com.weddingplanner.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/** Keep session permissions synchronized with the current persisted account role. */
@Component
public class SessionRoleSyncFilter extends OncePerRequestFilter {
    private final UserRepository users;

    public SessionRoleSyncFilter(UserRepository users) {
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") instanceof String id) {
            users.findById(id).ifPresentOrElse(
                user -> session.setAttribute("role", user.getRole()),
                session::invalidate
            );
        }
        chain.doFilter(request, response);
    }
}
