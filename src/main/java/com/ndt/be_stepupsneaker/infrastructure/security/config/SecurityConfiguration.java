package com.ndt.be_stepupsneaker.infrastructure.security.config;

import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/", "/error/**", "/auth/**", "/client/products/**",
                                        "/client/product-details/**", "/admin/notifications/sse/**","/client/product/**",
                                        "/admin/notifications", "/client/transaction/**", "/client/orders/**")
                                .permitAll()
                                .requestMatchers("/admin/customers/**", "/admin/orders/**", "/admin/order-details/**", "/admin/statistic/**",
                                        "/admin/return-forms/**")
                                .hasAnyRole(EntityProperties.STAFF, EntityProperties.ADMIN)
                                .requestMatchers(HttpMethod.GET
                                        , "/admin/order-histories/**", "/admin/voucher-histories/**"
                                        , "/admin/product-details/**", "/admin/trade-marks/**", "/admin/products/**"
                                        , "/admin/payments/**", "/admin/payment-methods/**", "/admin/colors/**"
                                        , "/admin/brands/**", "/admin/styles/**", "/admin/soles/**", "/admin/vouchers/**"
                                        , "/admin/sizes/**", "/admin/materials/**", "/admin/product/reviews/**")
                                .hasAnyRole(EntityProperties.STAFF, EntityProperties.ADMIN)
                                .requestMatchers("/admin/**").hasRole(EntityProperties.ADMIN)
                                .requestMatchers("/client/**").hasAnyRole(EntityProperties.CUSTOMER)
                                .requestMatchers("/auth/client/me").hasAnyRole(EntityProperties.CUSTOMER)
                                .requestMatchers("/auth/admin/me").hasAnyRole(EntityProperties.STAFF, EntityProperties.ADMIN)
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
