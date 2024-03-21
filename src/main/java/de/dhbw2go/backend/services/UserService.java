package de.dhbw2go.backend.services;

import de.dhbw2go.backend.entities.Role;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.exceptions.user.UserNameNotAvailableException;
import de.dhbw2go.backend.repositories.RoleRepository;
import de.dhbw2go.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private Role userRole;

    @Override
    public User loadUserByUsername(final String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username was not found: " + username));
    }

    public User createUser(final String username, final String password, final String name, final String location, final String faculty, final String program, final String course) throws UserNameNotAvailableException {
        if (!this.userRepository.existsByUsername(username)) {
            final User user = new User();
            user.setUsername(username);
            user.setPassword(UserService.B_CRYPT_PASSWORD_ENCODER.encode(password));
            user.setName(name);
            user.setLocation(location);
            user.setFaculty(faculty);
            user.setProgram(program);
            user.setCourse(course);
            user.addRole(this.getUserRole());
            return this.userRepository.save(user);
        }
        throw new UserNameNotAvailableException(username);
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

    public User changeUserDetails(final User user, final String name, final String location, final String faculty, final String program, final String course, final String image) {
        if (name != null) {
            user.setName(name);
        }
        if (location != null) {
            user.setLocation(location);
        }
        if (faculty != null) {
            user.setFaculty(faculty);
        }
        if (program != null) {
            user.setProgram(program);
        }
        if (course != null) {
            user.setCourse(course);
        }
        user.setImage(image);
        return this.userRepository.save(user);
    }

    public boolean checkUserByUsername(final String username) {
        return this.userRepository.existsByUsername(username);
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
