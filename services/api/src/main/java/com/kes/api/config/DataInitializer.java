package com.kes.api.config;

import com.kes.api.entity.Role;
import com.kes.api.entity.User;
import com.kes.api.repository.RoleRepository;
import com.kes.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));
            Role userRole = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(new Role(null, "USER")));
            Role editorRole = roleRepository.findByName("EDITOR").orElseGet(() -> roleRepository.save(new Role(null, "EDITOR")));

            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(new User(null, "admin", passwordEncoder.encode("admin123"), Set.of(adminRole)));
            }
            if (userRepository.findByUsername("user").isEmpty()) {
                userRepository.save(new User(null, "user", passwordEncoder.encode("user123"), Set.of(userRole)));
            }
            if (userRepository.findByUsername("editor").isEmpty()) {
                userRepository.save(new User(null, "editor", passwordEncoder.encode("editor123"), Set.of(editorRole)));
            }
        };
    }
}
