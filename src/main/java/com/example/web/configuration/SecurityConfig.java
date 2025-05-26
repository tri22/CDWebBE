package com.example.web.configuration;

import java.util.Arrays;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, ApiEndPoint.PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, ApiEndPoint.PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, ApiEndPoint.PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, ApiEndPoint.PUBLIC_ENDPOINTS).permitAll()

                        // Bảo vệ các endpoint yêu cầu ROLE_ADMIN
                        .requestMatchers(HttpMethod.GET, ApiEndPoint.ADMIN_ENDPOINTS).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, ApiEndPoint.ADMIN_ENDPOINTS).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, ApiEndPoint.ADMIN_ENDPOINTS).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ApiEndPoint.ADMIN_ENDPOINTS).hasAuthority("ADMIN")

                        // Bảo vệ mọi endpoint còn lại yêu cầu phải xác thực
                        .anyRequest().authenticated()

                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(customJwtAuthentication()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // Đảm bảo xử lý lỗi ở đây
                );
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter customJwtAuthentication() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("Role");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173/")); // hoặc domain FE của bạn
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
