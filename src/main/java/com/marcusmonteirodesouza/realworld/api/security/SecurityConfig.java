package com.marcusmonteirodesouza.realworld.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfCustomizer -> csrfCustomizer.disable())
                .authorizeHttpRequests(
                        authorizeHttpRequests ->
                                authorizeHttpRequests
                                        .requestMatchers(HttpMethod.POST, "/users")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.POST, "/users/login")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/profiles/*")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/articles")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/articles/*")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/articles/*/comments")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/tags")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated());

        http.sessionManagement(
                sessionManagementCustomizer ->
                        sessionManagementCustomizer.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS));

        http.oauth2ResourceServer(
                oauth2ResourceServerCustomizer ->
                        oauth2ResourceServerCustomizer
                                .bearerTokenResolver(
                                        httpServletRequest -> {
                                            var header =
                                                    httpServletRequest.getHeader(
                                                            HttpHeaders.AUTHORIZATION);

                                            if (header == null || header.isBlank()) {
                                                return header;
                                            }

                                            var token = header.split("Token ")[1].trim();

                                            return token;
                                        })
                                .jwt(Customizer.withDefaults()));

        return http.build();
    }
}
