package de.dhbw2go.backend.security.jwt;

import de.dhbw2go.backend.security.SecurityUserDetails;
import de.dhbw2go.backend.security.SecurityUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationTokenFilter.class);
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String jwt = this.jwtHelper.getJWTFromCookies(httpServletRequest);
            if (jwt != null && this.jwtHelper.validateJWT(jwt)) {
                final String email = this.jwtHelper.getEmailFromJWT(jwt);
                final SecurityUserDetails securityUserDetails = this.userDetailsService.loadUserByUsername(email);
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(securityUserDetails, null,
                        securityUserDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (final Exception exception) {
            JWTAuthenticationTokenFilter.logger.error("Unable to set user authentication: " + exception.getMessage());
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
