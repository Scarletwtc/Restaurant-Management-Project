package com.example.mvcproducts.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ProviderManager authManagerBean(HttpSecurity security) throws Exception {
        return (ProviderManager) security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authProvider())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
                        .requestMatchers("/", "/css/**", "/js/**", "/login", "/register").permitAll()
                        // registration form submit
                        .requestMatchers(HttpMethod.POST, "/register", "/users/register/**").permitAll()

                        // Chef permissions
                        .requestMatchers(HttpMethod.POST,   "/ingredients/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.PUT,    "/ingredients/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.DELETE, "/ingredients/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.GET,    "/ingredients/**").hasAnyRole("CHEF","OWNER")

                        .requestMatchers(HttpMethod.POST,   "/recipes/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.PUT,    "/recipes/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.DELETE, "/recipes/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.GET,    "/recipes/**").hasAnyRole("CHEF","OWNER")

                        .requestMatchers(HttpMethod.POST,   "/kitchen-orders/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.PUT,    "/kitchen-orders/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.GET,    "/kitchen-orders/**").hasAnyRole("CHEF","OWNER")

                        .requestMatchers(HttpMethod.POST,   "/waste-reports/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.PUT,    "/waste-reports/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.GET,    "/waste-reports/**").hasAnyRole("CHEF","OWNER")

                        .requestMatchers(HttpMethod.POST, "/purchase-orders/*/approve", "/purchase-orders/*/reject").hasRole("SUPPLIER")
                        .requestMatchers(HttpMethod.POST, "/purchase-orders/**").hasRole("CHEF")
                        .requestMatchers(HttpMethod.GET, "/purchase-orders", "/purchase-orders/**").hasAnyRole("CHEF", "SUPPLIER", "OWNER")
                        .requestMatchers("/reports/**", "/dashboard/**").hasRole("OWNER")

                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
