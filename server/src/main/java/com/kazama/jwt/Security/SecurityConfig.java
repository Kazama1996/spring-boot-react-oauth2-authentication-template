// package com.kazama.jwt.Security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import
// org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import
// org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.AuthenticationEntryPoint;
// import org.springframework.security.web.SecurityFilterChain;
// import
// org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.servlet.HandlerExceptionResolver;

// import com.kazama.jwt.dao.UserRepository;

// import static org.springframework.security.config.Customizer.withDefaults;

// import lombok.AllArgsConstructor;
// import lombok.RequiredArgsConstructor;

// @Configuration
// @EnableWebSecurity
// @AllArgsConstructor
// public class SecurityConfig {

// private JwtFilter jwtFilter;

// private UserRepository userRepository;

// @Autowired
// @Qualifier("delegatedAuthenticationEntryPoint")
// private AuthenticationEntryPoint authenticationEntryPoint;

// @Bean
// protected UserDetailsService userDetailsService() {
// // return username ->
// // userRepository.findById(UUID.fromString(username)).orElseThrow(()-> new
// // UsernameNotFoundException("User not found"));
// return email -> userRepository.findByEmail(email)
// .orElseThrow(() -> new UsernameNotFoundException("User not found"));
// }

// @Bean
// protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception
// {

// http.csrf(csrf -> csrf.disable());
// http.authorizeHttpRequests(
// auth ->
// auth.requestMatchers("/api/v1/auth/**").permitAll().anyRequest().authenticated());
// http.httpBasic(withDefaults());

// http.httpBasic(basic ->
// basic.authenticationEntryPoint(authenticationEntryPoint));
// http.authenticationProvider(authenticationProvider());
// http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

// return http.build();
// }

// @Bean
// public PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder(10);
// }

// @Bean
// public AuthenticationProvider authenticationProvider() {
// DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
// provider.setPasswordEncoder(passwordEncoder());
// provider.setUserDetailsService(userDetailsService());
// return provider;
// }

// @Bean
// public AuthenticationManager
// authenticationManager(AuthenticationConfiguration config) throws Exception {
// return config.getAuthenticationManager();
// }

// }
