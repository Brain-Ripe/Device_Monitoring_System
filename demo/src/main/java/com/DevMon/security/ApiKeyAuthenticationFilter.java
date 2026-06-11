package com.DevMon.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    // In production, move this to application.properties or your DB device registry
    private final String VALID_API_KEY = "devmon-secure-agent-token-xyz123";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestApiKey = request.getHeader("X-API-KEY");

        // If the agent passed an API Key and it matches our system record
        if (requestApiKey != null && requestApiKey.equals(VALID_API_KEY)) {

            // Create a specialized system authority for background daemons
            UsernamePasswordAuthenticationToken daemonAuth = new UsernamePasswordAuthenticationToken(
                    "DAEMON_AGENT",
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_AGENT"))
            );

            // Authenticate the request context immediately
            SecurityContextHolder.getContext().setAuthentication(daemonAuth);
        }

        // Continue down the chain (if authenticated by API key, next filters will skip check)
        filterChain.doFilter(request, response);
    }
}