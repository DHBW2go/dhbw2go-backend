package de.dhbw2go.backend.security;

import de.dhbw2go.backend.security.jwt.JWTAuthenticationEntryPoint;
import de.dhbw2go.backend.security.jwt.JWTAuthenticationTokenFilter;
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
    private SecurityUserDetailsService securityUserDetailsService;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/error").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/api-docs").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/api-docs/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/swagger-ui").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/swagger-ui/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/authentication/check/**", "/authentication/register", "/authentication/login").permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated().and().addFilterBefore(this.jwtAuthenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
                });
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(final HttpSecurity httpSecurity) throws Exception {
        final AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.securityUserDetailsService).passwordEncoder(this.passwordEncoder());
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
