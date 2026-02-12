package com.techtricks.coe_auth.filters;

import com.techtricks.coe_auth.services.CustomUserDetailsService;
import com.techtricks.coe_auth.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;



//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        // ✅ VERY IMPORTANT: exit early if no token
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        if (!jwtUtil.validateToken(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//
//        String email = jwtUtil.extractEmail(token);
//        String role = jwtUtil.extractRole(token);
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
//            List<GrantedAuthority> authorities =
//                    List.of(new SimpleGrantedAuthority("ROLE_" + role));
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            email,   // principal
//                            userDetails,
//                            authorities
//                    );
//
//            authentication.setDetails(
//                    new WebAuthenticationDetailsSource().buildDetails(request)
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        String role = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            email = jwtUtil.extractEmail(token);
            role = jwtUtil.extractRole(token);
        }

        if(email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null){
            //TODO fetch user by username

            UserDetails userdetails = customUserDetailsService.loadUserByUsername(email);

            if(jwtUtil.validateToken(
                    userdetails.getUsername(),
                    userdetails ,
                    token)){


                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userdetails, null,
                                authorities);
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().
                                buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }


        filterChain.doFilter(request, response);

//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//
//        if(authHeader == null && authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//
//
//        String token = authHeader.substring(7);
//        if(!jwtUtil.validateToken(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String email = jwtUtil.extractEmail(token);
//        String role = jwtUtil.extractRole(token);
//
//        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//
//            List<GrantedAuthority> authorities =
//                    List.of(new SimpleGrantedAuthority
//                            ("ROLE_" + role));
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            email,
//                            null,
//                            authorities);
//
//            authentication.setDetails(new WebAuthenticationDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//
//
//        filterChain.doFilter(request, response);
//    }
}}
