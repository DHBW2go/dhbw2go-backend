package de.dhbw2go.backend.configuration;

import de.dhbw2go.backend.jwt.JWTAuthenticationEntryPoint;
import de.dhbw2go.backend.jwt.JWTAuthenticationTokenFilter;
import de.dhbw2go.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/error").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/docs").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/docs/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/swagger-ui").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/swagger-ui/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/user/check/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/authentication/register", "/authentication/login", "/authentication/refresh").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/dualis/cookie", "/dualis/overview", "/dualis/semesters", "/dualis/semester/**", "/dualis/exams/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated().and().addFilterBefore(this.jwtAuthenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
                });
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(final HttpSecurity httpSecurity) throws Exception {
        final AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userService).passwordEncoder(this.passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationTokenFilter jwtAuthenticationTokenFilterBean() {
        return new JWTAuthenticationTokenFilter();
    }
}
