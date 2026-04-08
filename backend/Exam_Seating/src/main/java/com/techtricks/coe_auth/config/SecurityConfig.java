package com.techtricks.coe_auth.config;

import com.techtricks.coe_auth.filters.JwtAuthFilter;
import com.techtricks.coe_auth.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .cors(cors ->{})
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth


                                .requestMatchers("/api/v1/student/me").hasRole("STUDENT")
                                .requestMatchers("/api/v1/appointments/my","/api/v1/appointments/book").hasRole("STUDENT")
                                .requestMatchers("/api/v1/appointments").hasRole("ADMIN")
                                .requestMatchers("/api/v1/departments").hasRole("ADMIN")
                                .requestMatchers("/api/v1/examrooms/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/blackRoom/assign/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/exam/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/subjects/bulk").hasRole("ADMIN")
                                .requestMatchers("/api/v1/admin/generate").hasRole("ADMIN")
                                .requestMatchers("/api/v1/users/staff/me").hasRole("STAFF")
                                .requestMatchers("/api/v1/auth/register" ,
                                        "/api/v1/auth/**" ,
                                        "/actuator/health",
                                        "/api/v1/seats/**",
                                        "/api/v1/blackRoom/access/**",
                   "/api/v1/student/**",
                                        "/api/v1/invigilator/**",
                                        "/api/v1/student/add").permitAll()
                                .anyRequest().authenticated());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8081")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
