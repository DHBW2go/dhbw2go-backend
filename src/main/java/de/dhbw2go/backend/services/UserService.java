package de.dhbw2go.backend.services;

import de.dhbw2go.backend.entities.Role;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.repositories.RoleRepository;
import de.dhbw2go.backend.repositories.UserRepository;
import de.dhbw2go.backend.security.SecurityUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private Role userRole;

    @Override
    public SecurityUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new SecurityUserDetails(user, this.mapRolesToAuthorities(user.getRoles()));
    }

    public User createUser(final String username, final String password) {
        if (!this.userRepository.existsByUsername(username)) {
            final User user = new User();
            user.setUsername(username);
            user.setPassword(UserService.B_CRYPT_PASSWORD_ENCODER.encode(password));
            user.addRole(this.getUserRole());
            return this.userRepository.save(user);
        }
        return null;
    }

    public boolean changeUserPassword(final User user, final String newPassword) {
        if (UserService.B_CRYPT_PASSWORD_ENCODER.matches(user.getPassword(), newPassword)) {
            final String encodedNewPassword = UserService.B_CRYPT_PASSWORD_ENCODER.encode(newPassword);
            user.setPassword(encodedNewPassword);
            this.userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean checkUserByUsername(final String username) {
        return this.userRepository.existsByUsername(username);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getType().name())).collect(Collectors.toList());
    }

    private Role getUserRole() {
        if (userRole == null) {
            this.userRole = this.roleRepository.findByType(Role.RoleType.USER).orElseGet(() -> {
                final Role role = new Role();
                role.setType(Role.RoleType.USER);
                return this.roleRepository.save(role);
            });
        }
        return this.userRole;
    }
}
