package com.nashtech.rookies.java05.AssetManagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class SecurityConfig {
    private static final String STAFF = "Staff";
    private static final String ADMIN = "Admin";
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "swagger-ui/**"
    };

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SecurityContext securityContext() {
        return SecurityContextHolder.getContext();
    }

    //Authorize here
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/api/assets/**").hasAnyAuthority(ADMIN)
                .antMatchers("/api/assets/states/**").hasAnyAuthority(ADMIN)
                .antMatchers("/api/categories/**").hasAnyAuthority(ADMIN)
                .antMatchers("/api/categories/name/**").hasAnyAuthority(ADMIN)
                .antMatchers("/api/users/reset-password").authenticated()
                .antMatchers("/api/users/**").hasAnyAuthority(ADMIN)


                .antMatchers(HttpMethod.PUT, "/api/assignments/**/state").hasAnyAuthority(STAFF,ADMIN)
                .antMatchers("/api/reports").hasAnyAuthority(ADMIN)
//categories
                .antMatchers(HttpMethod.POST, "/api/categories").hasAnyAuthority(ADMIN)
//assets
                .antMatchers(HttpMethod.POST, "/api/assets").hasAnyAuthority(ADMIN)
                .antMatchers(HttpMethod.PUT, "/api/assets/**").hasAnyAuthority(ADMIN)
//locations
                .antMatchers(HttpMethod.GET, "/api/locations").hasAnyAuthority(ADMIN)
//roles
                .antMatchers(HttpMethod.GET, "/api/roles").hasAnyAuthority(ADMIN)
//users
                .antMatchers(HttpMethod.POST, "/api/users").hasAnyAuthority(ADMIN)
                .antMatchers(HttpMethod.PUT, "/api/users/**").hasAnyAuthority(ADMIN)
//assignments
                .antMatchers(HttpMethod.POST, "/api/assignments").hasAnyAuthority(ADMIN)
                .antMatchers(HttpMethod.PUT, "/api/assignments/**").hasAnyAuthority(ADMIN)
                .antMatchers(HttpMethod.DELETE, "/api/assignments/**").hasAnyAuthority(ADMIN)

                .antMatchers(HttpMethod.PUT, "/api/assignments/**/state").authenticated()

//Returning

                .antMatchers(HttpMethod.POST, "/api/returns").hasAnyAuthority(ADMIN, STAFF)
                .antMatchers(HttpMethod.PUT, "/api/returns/**").hasAnyAuthority(ADMIN)
                .antMatchers("/api/**").authenticated();
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
