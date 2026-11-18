package com.example.sistemadescarte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/login",
                    "/logout"
                )
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login.html",
                    "/favicon.svg",
                    "/style.css",
                    "/css/**",
                    "/js/**",
                    "/build/**"
                ).permitAll()

                .requestMatchers("/usuarios/**", "/admin/usuarios")
                .hasRole("ADMIN")

                .requestMatchers("/admin/permissoes", "/permissoes", "/permissoes/**")
                .hasRole("ADMIN")

                .requestMatchers(
                    "/cadastro-produto",
                    "/cadastro-produto.html",
                    "/efetuar-descarte",
                    "/efetuar-descarte.html",
                    "/produtos/*/visualizar",
                    "/produtos/*/editar"
                ).hasAnyRole("ADMIN", "ALMOXARIFADO")

                .requestMatchers(HttpMethod.POST, "/produtos", "/produtos/**")
                .hasAnyRole("ADMIN", "ALMOXARIFADO")

                .requestMatchers(HttpMethod.PUT, "/produtos/**")
                .hasAnyRole("ADMIN", "ALMOXARIFADO")

                .requestMatchers(HttpMethod.PATCH, "/produtos/**")
                .hasAnyRole("ADMIN", "ALMOXARIFADO")

                .requestMatchers(HttpMethod.DELETE, "/produtos/**")
                .hasAnyRole("ADMIN", "ALMOXARIFADO")

                .requestMatchers("/admin/empresas-coletoras", "/empresas-coletoras", "/empresas-coletoras/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/coletas/**", "/descartes/**")
                .hasAnyRole("ADMIN", "ALMOXARIFADO")
                .requestMatchers(HttpMethod.GET, "/coletas/**")
                .hasAnyRole("ADMIN", "ALMOXARIFADO", "ANALISTA")
                .requestMatchers("/consulta-coletas")
                .hasAnyRole("ADMIN", "ALMOXARIFADO", "ANALISTA")
                .anyRequest()
                .authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index", true)
                .failureUrl("/login.html?erro")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

