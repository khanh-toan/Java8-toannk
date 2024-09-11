package org.shopping.config;

import org.shopping.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login", "/admin/logout", "/403", "/styles.css")
                        .permitAll() // Cho phép truy cập mà không cần xác thực
                        .requestMatchers("/admin/orderList", "/admin/order", "/admin/accountInfo")
                        .hasAnyRole("EMPLOYEE", "MANAGER")
                        .requestMatchers("/admin/product")
                        .hasRole("MANAGER")
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e.accessDeniedPage("/403"))
                .formLogin(login -> login
                        .loginProcessingUrl("/j_spring_security_check")
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/accountInfo")
                        /*.defaultSuccessUrl("/test")*/
                        .failureUrl("/admin/login?error=true")
                        .usernameParameter("userName")
                        .passwordParameter("password"))
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/"));

        return http.build();
    }
}
