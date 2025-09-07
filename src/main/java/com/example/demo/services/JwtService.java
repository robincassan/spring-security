package com.example.demo.services;

import com.example.demo.models.UserApp;
import com.example.demo.repositories.UserAppRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
public class JwtService extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.cookie_name}")
    private String cookieName;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    @Autowired
    UserAppRepository userAppRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getCookies() != null) {
            Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .map(Cookie::getValue)
                    .forEach(token -> {
                        try {
                            logger.info("Processing JWT token: {}", token);
                            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
                            logger.info("Claims: subject={}, role={}", claims.getSubject(), claims.get("role"));

                            Optional<UserApp> optUserApp = userAppRepository.findByUsername(claims.getSubject());
                            if (optUserApp.isEmpty()) {
                                logger.error("User not found: {}", claims.getSubject());
                                throw new UsernameNotFoundException(claims.getSubject());
                            }
                            UserApp userApp = optUserApp.get();

                            if (validateToken(token, userApp)) {
                                String role = claims.get("role", String.class);
                                logger.info("Setting authority: ROLE_{}", role);
                                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                                UsernamePasswordAuthenticationToken authToken =
                                        new UsernamePasswordAuthenticationToken(userApp, null, Collections.singletonList(authority));

                                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                SecurityContextHolder.getContext().setAuthentication(authToken);
                                logger.info("Authentication set for user: {}", userApp.getUsername());
                            } else {
                                logger.warn("Invalid token for user: {}", userApp.getUsername());
                            }

                        } catch (Exception e) {
                            logger.error("Error processing JWT: {}", e.getMessage());
                            Cookie expiredCookie = new Cookie(cookieName, null);
                            expiredCookie.setPath("/");
                            expiredCookie.setHttpOnly(true);
                            expiredCookie.setMaxAge(0);
                            response.addCookie(expiredCookie);
                        }
                    });
        } else {
            logger.warn("No cookies found in request");
        }

        filterChain.doFilter(request, response);
    }

    public Boolean validateToken(String token, UserApp userApp) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims.getSubject().equals(userApp.getUsername()) && !isTokenExpired(claims);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public String generateToken(UserApp userApp) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userApp.getUsername());
        claims.put("role", userApp.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userApp.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public ResponseCookie createAuthenticationToken(UserApp userApp) throws Exception {
        try {
            final String token = generateToken(userApp);
            return ResponseCookie.from(cookieName, token)
                    .httpOnly(true)
                    .path("/")
                    .build();
        } catch (DisabledException e) {
            throw new Exception("User account is disabled");
        }
    }
}