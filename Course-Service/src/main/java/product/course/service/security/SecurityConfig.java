package product.course.service.security;

import org.springframework.context.annotation.Bean;

public class SecurityConfig {

    private final GatewayHeaderAuthenticationFilter gatewayHeaderAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/courses/**").authenticated() // Tất cả các request tới API này đều cần đi qua bộ lọc xác thực
                        .anyRequest().permitAll()
                )
                // Nhúng filter đọc Header của chúng ta vào trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(gatewayHeaderAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
