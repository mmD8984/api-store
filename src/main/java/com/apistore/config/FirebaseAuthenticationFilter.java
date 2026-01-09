package com.apistore.config;

import com.apistore.service.FirebaseAuthService;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseAuthService firebaseAuthService;

    public FirebaseAuthenticationFilter(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idToken = authHeader.replace("Bearer ", "");

            try {
                FirebaseToken decodedToken = firebaseAuthService.verifyIdToken(idToken);
                String firebaseUid = decodedToken.getUid();

                // Inject firebaseUid cho Controller dùng
                request.setAttribute("firebaseUid", firebaseUid);

                // Set Authentication cho Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                firebaseUid,
                                null,
                                Collections.emptyList()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid Firebase Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

