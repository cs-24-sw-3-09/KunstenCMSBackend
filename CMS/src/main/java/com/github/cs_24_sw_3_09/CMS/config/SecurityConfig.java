package com.github.cs_24_sw_3_09.CMS.config;

import com.github.cs_24_sw_3_09.CMS.filter.JwtAuthFilter;
import com.github.cs_24_sw_3_09.CMS.services.UserService;

import java.util.Arrays;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity(
        securedEnabled = true
)
public class SecurityConfig {

    private final JwtAuthFilter authFilter;
    private final UserService userService;
    private final ArrayList<String> allowedOrigins;
    
    @Value("${FRONTEND.URL:localhost}")
    private String frontendUrl;

    @Autowired
    public SecurityConfig(JwtAuthFilter authFilter, UserService userService) {
        this.userService = userService;
        this.authFilter = authFilter;
        this.allowedOrigins = new ArrayList<>();
        this.allowedOrigins.add("http://localhost:5173");
        this.allowedOrigins.add(frontendUrl);

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                //Allow cors preflight    
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //Allow files
                .requestMatchers(HttpMethod.GET, "/files/visual_media/**").permitAll()
                //Allow auth request without token
                .requestMatchers(HttpMethod.POST, "/api/account/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/account/reset-password").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/account/reset-password/new").permitAll()
                //Need to be authenticated for all other routes
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            // Add our security auth filter.
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}