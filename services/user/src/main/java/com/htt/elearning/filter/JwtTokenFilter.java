package com.htt.elearning.filter;

import com.htt.elearning.component.JwtTokenUtil;
import com.htt.elearning.user.pojo.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
//    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtTokenUtil.extractUsername(token);
        String role = jwtTokenUtil.extractClaim(token, claims -> (String) claims.get("role"));

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
//    protected void doFilterInternal(
//            @NotNull HttpServletRequest request,
//            @NotNull HttpServletResponse response,
//            @NotNull FilterChain filterChain) throws ServletException, IOException {
//
//        try {
//            if(isBypassToken(request)) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            final String authHeader = request.getHeader("Authorization");
//            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                return;
//            }
//            final String token = authHeader.substring(7);
//            final String username = jwtTokenUtil.extractUsername(token);
//
//            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                User userDetails = (User) userDetailsService.loadUserByUsername(username);
//
//                if (username != null && jwtTokenUtil.validateToken(token, userDetails)) {
//                    String role = jwtTokenUtil.extractRole(token);
//                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role)); // Ví dụ: ROLE_ADMIN
//
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            username,
//                            null,
//                            authorities
//                    );
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//
//            }
//            filterChain.doFilter(request, response);
//
//        } catch (Exception ex){
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//        }
//    }

    //    tách riêng những api không cần đăng nhập vẫn xem được.
//    private boolean isBypassToken(@NotNull HttpServletRequest request) {
//        final List<Pair<String, String>> bypassTokens = Arrays.asList(
//                Pair.of("/api/courses", "GET"),
//                Pair.of("/api/assignments/lesson/", "GET"),
//                Pair.of("/api/questions/count/assignment/", "GET"),
//                Pair.of("/api/lessons/get-first-lesson/course/", "GET"),
//                Pair.of("/api/categories", "GET"),
//                Pair.of("/api/teachers", "GET"),
//                Pair.of("/api/users/register", "POST"),
//                Pair.of("/api/users/login", "POST"),
//                Pair.of("/api/login/auth/social/callback", "GET"),
//                Pair.of("/api/login/auth/social-login", "GET")
//        );
//
//        for(Pair<String, String> bypassToken : bypassTokens) {
//            if(request.getServletPath().contains(bypassToken.getFirst()) &&
//                    request.getMethod().equals(bypassToken.getSecond())) {
//                return true;
//            }
//        }
//        return false;
//    }
}
