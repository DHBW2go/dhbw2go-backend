package de.dhbw2go.backend.security;

import de.dhbw2go.backend.entities.Role;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public SecurityUserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = this.userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));
        return new SecurityUserDetails(user.getEmail(), user.getPassword(), this.mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getType().name())).collect(Collectors.toList());
    }
}
