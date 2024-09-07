package com.sash.ticketing_system.services;

import com.sash.ticketing_system.config.UserNotFoundException;
import com.sash.ticketing_system.models.User;
import com.sash.ticketing_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

            @Transactional
            public void createUser(User user) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }

            public User findByUserId(Long id) {
                return userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
            }


            public List<User> findAllUsers() {
                return userRepository.findAll();
            }

            @Transactional
            public User updateUser(Long id, User updatedUser) {
                return userRepository.findById(id)
                        .map(user -> {
                            user.setUsername(updatedUser.getUsername());
                            user.setPassword(updatedUser.getPassword()); // Consider hashing passwords
                            user.setRole(updatedUser.getRole());
                            return userRepository.save(user);
                        })
                        .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
            }

            @Transactional
            public void deleteUser(Long id) {
                if (!userRepository.existsById(id)) {
                    throw new UserNotFoundException("User not found with id " + id);
                }
                userRepository.deleteById(id);
            }

    public UserDetails findByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}


