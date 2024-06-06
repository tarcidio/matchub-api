package com.matchhub.matchhub.security;

import com.matchhub.matchhub.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.matchhub.matchhub.security.token.domain.enums.Permission.*;
import static com.matchhub.matchhub.security.token.domain.enums.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityFilterConfig {
    private static final String[] WHITE_LIST_URL = {
            // Everyone has the right to try to authenticate themselves
            "/auth/**",
            // Everyone can read screen comments
            "/screens/*",
            "/screens/*/*",
            // Use h2
            "/h2-console/**",
            // Use Swagger 2 and 3
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            // Use Swagger UI
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/configuration/ui",
            // Use Swagger UI Security
            "/configuration/security",
            // Swagger UI Dependency
            "/webjars/**"
            };

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsConfigurationSource corsConfigurationSource;
    private final LogoutHandler logoutHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
                            /* ROLE: HUB USERS */
                                /* HUB USERS */
                                // Roles
                            .requestMatchers("/hubusers/**").
                                hasAnyRole(ADMIN.name(), MODERATOR.name(), HUBUSER.name(), GUEST.name())
                                // Authority
                            .requestMatchers(GET, "/hubusers/**").
                                hasAnyAuthority(HUBUSER_READ.name())
                            .requestMatchers(PUT, "/hubusers/**").
                                hasAnyAuthority(HUBUSER_UPDATE.name())
                            .requestMatchers(PATCH, "/hubusers/confirm/**").
                                hasAnyAuthority(GUEST_CONFIRM.name())
                            .requestMatchers(PATCH, "/hubusers/**").
                                hasAnyAuthority(HUBUSER_PATCH.name())
                            .requestMatchers(POST, "/hubusers/**").
                                hasAnyAuthority(HUBUSER_CREATE.name())
                                /* SCREENS */
                                // Roles
                            .requestMatchers("/screens/*/comments/**").
                                hasAnyRole(ADMIN.name(), MODERATOR.name(), HUBUSER.name())
                                // Authority
                            .requestMatchers(GET, "/screens/*/comments/**").
                                hasAnyAuthority(HUBUSER_READ.name())
                            .requestMatchers(PUT, "/screens/*/comments/**").
                                hasAnyAuthority(HUBUSER_UPDATE.name())
                            .requestMatchers(POST, "/screens/*/comments/**").
                                hasAnyAuthority(HUBUSER_CREATE.name())
                            .requestMatchers(DELETE, "/screens/*/comments/**").
                                hasAnyAuthority(HUBUSER_DELETE.name())
                                /* EVALUATIONS */
                                // Roles
                            .requestMatchers("/comments/*/evaluations/**").
                                hasAnyRole(ADMIN.name(), MODERATOR.name(), HUBUSER.name())
                                // Authority
                            .requestMatchers(PUT, "/comments/*/evaluations/**").
                                hasAnyAuthority(HUBUSER_UPDATE.name())
                            .requestMatchers(POST, "/comments/*/evaluations/**").
                                hasAnyAuthority(HUBUSER_CREATE.name())
                            .requestMatchers(DELETE, "/comments/*/evaluations/**").
                                hasAnyAuthority(HUBUSER_DELETE.name())
                                /* CHAMPIONS */
                                // Roles
                            .requestMatchers("/champions/**").
                                hasAnyRole(ADMIN.name(), MODERATOR.name(), HUBUSER.name())
                                // Authority
                            .requestMatchers(GET, "/champions/**").
                                hasAnyAuthority(HUBUSER_READ.name())
                                /* RSO */
                                // Roles
                            .requestMatchers("/rso/**").
                                hasAnyRole(ADMIN.name(), MODERATOR.name(), HUBUSER.name())
                                // Authority
                            .requestMatchers(GET, "/rso/**").
                                hasAnyAuthority(HUBUSER_READ.name())
                            /* ROLE: MODERATORS */
                                // Roles
                            .requestMatchers("/moderators/**").
                                hasAnyRole(ADMIN.name(), MODERATOR.name())
                                // Authority
                            .requestMatchers(PUT, "/moderators/**").
                                hasAnyAuthority(MODERATOR_UPDATE.name())
                            .requestMatchers(DELETE, "/moderators/**").
                                hasAnyAuthority(MODERATOR_DELETE.name())
                            /* ROLE: ADMINS */
                                // Roles
                            .requestMatchers("/admins/**").
                                hasAnyRole(ADMIN.name())
                                // Authority
                            .requestMatchers(PUT, "/admins/**").
                                hasAnyAuthority(ADMIN_UPDATE.name())
                            .requestMatchers(DELETE, "/admins/**").
                                hasAnyAuthority(ADMIN_DELETE.name())
                            .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .addLogoutHandler(new CookieClearingLogoutHandler("refreshToken"))
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );
        return http.build();
    }
}

