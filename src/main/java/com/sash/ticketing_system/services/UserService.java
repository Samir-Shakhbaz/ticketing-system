package com.sash.ticketing_system.services;

import com.sash.ticketing_system.config.UserNotFoundException;
import com.sash.ticketing_system.models.User;
import com.sash.ticketing_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

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
                return userRepository.findById(id).orElse(null);
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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User findById(Long assigneeId) {return userRepository.findById(assigneeId).orElse(null);
    }
}


