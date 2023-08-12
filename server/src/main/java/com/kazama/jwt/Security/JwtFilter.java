// package com.kazama.jwt.Security;

// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.kazama.jwt.model.User;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.AllArgsConstructor;
// import lombok.RequiredArgsConstructor;

// @Component
// @AllArgsConstructor
// public class JwtFilter extends OncePerRequestFilter {

// private JwtService jwtService;

// private UserDetailsService userDetailsService;

// @Override
// protected void doFilterInternal(HttpServletRequest request,
// HttpServletResponse response, FilterChain filterChain)
// throws ServletException, IOException {

// Cookie[] cookies = request.getCookies();
// String cookieName = "jwt";
// String jwtToken = null;
// String userId;
// for (Cookie cookie : cookies) {
// if (cookie.getName().equals(cookieName)) {
// jwtToken = cookie.getValue();
// break;
// }
// }

// if (jwtToken == null) {
// filterChain.doFilter(request, response);
// return;
// }
// userId = jwtService.extractUserId(jwtToken);
// if (userId != null && SecurityContextHolder.getContext().getAuthentication()
// == null) {
// User userDetails = (User) this.userDetailsService.loadUserByUsername(userId);
// if (jwtService.isTokenValid(jwtToken, userDetails)) {
// UsernamePasswordAuthenticationToken authToken = new
// UsernamePasswordAuthenticationToken(
// userDetails.getUserId(), null, userDetails.getAuthorities());
// authToken.setDetails(new
// WebAuthenticationDetailsSource().buildDetails(request));
// SecurityContextHolder.getContext().setAuthentication(authToken);
// }
// }

// filterChain.doFilter(request, response);
// }

// @Override
// protected boolean shouldNotFilter(HttpServletRequest request) throws
// ServletException {
// String path = request.getRequestURI();

// return path.startsWith("/api/v1/auth");
// }

// }
