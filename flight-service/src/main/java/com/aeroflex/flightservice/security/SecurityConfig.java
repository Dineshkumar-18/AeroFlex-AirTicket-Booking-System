package com.aeroflex.flightservice.security;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.common.security.JwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/flights/**", "/api/v1/airports/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/airlines/*/commission").hasRole(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/airlines/**").hasAnyRole(RoleType.USER.name(), RoleType.FLIGHT_OWNER.name(), RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/airlines/**").hasAnyRole(RoleType.FLIGHT_OWNER.name(), RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/airlines/**").hasAnyRole(RoleType.FLIGHT_OWNER.name(), RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/airlines/**").hasAnyRole(RoleType.FLIGHT_OWNER.name(), RoleType.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );

        return http.build();
    }
}
