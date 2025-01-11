package com.charitan.statistics.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINT = { "/donor/getAll",
           "/api-docs", "/v3/api-docs", "/api-docs.yaml", "/swagger-ui/**"};
    private final ProfileCookieFilter profileCookieFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll()
                )
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()) // Secure all other endpoints
//                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF if not needed
                .addFilterBefore(profileCookieFilter, UsernamePasswordAuthenticationFilter.class); // Add custom filter

        return httpSecurity.build();
    }
}
