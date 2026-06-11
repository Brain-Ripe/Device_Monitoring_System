package com.DevMon.service;
import com.DevMon.dto.AuthRequestDTO;
import com.DevMon.entity.User;
import com.DevMon.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    //ContractMethods

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("DevMon Security: User not found with username: " + username));
    }

    // Business Logic

    public User register(AuthRequestDTO request) {
        // Guard clause: Prevent duplicate users in PostgreSQL
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken inside DevMon registry!");
        }

        User user = new User();
        user.setUsername(request.getUsername());

        // CRITICAL: Never store raw text. Hash it using BCrypt before database persistence.
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }
}
