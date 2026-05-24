package identity.identityservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tự do truy cập các API Authentication công khai bao gồm cả /refresh
                        .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
