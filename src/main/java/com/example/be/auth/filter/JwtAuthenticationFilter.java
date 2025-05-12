package com.example.be.auth.filter;

import com.example.be.auth.jwt.JwtTokenProvider;
import com.example.be.auth.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(!(httpServletRequest.getHeader("Authorization") == null)) {
            String token = jwtTokenProvider.resolveToken(httpServletRequest);

            if (token != null && jwtTokenProvider.validateAccessToken(token)) {
                String email = jwtTokenProvider.getEmail(token);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
