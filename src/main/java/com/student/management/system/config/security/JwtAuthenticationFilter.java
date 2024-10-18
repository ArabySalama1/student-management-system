package com.student.management.system.config.security;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.management.system.model.dto.AuthFailureResponse;
import com.student.management.system.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.WebUtils.getCookie;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {

            final String jwt = getCookieValue(request);
            if (isBlank(jwt))
                throw new AuthenticationCredentialsNotFoundException("Missing access token");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && authentication == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            handleAuthenticationFailure(response, e);
        }
    }


    private String getCookieValue(HttpServletRequest request) {
        Cookie cookie = getCookie(request, "accessToken");
        return cookie != null ? cookie.getValue() : null;
    }

    private void handleAuthenticationFailure(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(constructAuthFailureResponse(e));
    }


    private String constructAuthFailureResponse(Exception e) throws JsonProcessingException {
        AuthFailureResponse authFailureResponse = AuthFailureResponse.builder().title(e.getMessage()).detail(e.getMessage()).httpStatus(UNAUTHORIZED.name()).build();

        return objectMapper.writeValueAsString(authFailureResponse);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return EXCLUDED_PATHS.stream().anyMatch(path -> pathMatcher.match(path, request.getServletPath()));
    }

}
