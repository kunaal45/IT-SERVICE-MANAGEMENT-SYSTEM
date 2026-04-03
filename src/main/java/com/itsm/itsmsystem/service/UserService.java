package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.CreateUserRequest;
import com.itsm.itsmsystem.enums.EngineerLevel;
import com.itsm.itsmsystem.enums.Role;
import com.itsm.itsmsystem.exception.ResourceNotFoundException;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    /**
     * Admin creates a new user with any role.
     */
    public User createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .engineerLevel(request.getEngineerLevel())
                .specializedCategories(request.getSpecializedCategories())
                .build();
        return userRepository.save(user);
    }

    /**
     * Admin updates a user's role.
     */
    public User updateUserRole(Long userId, Role newRole) {
        User user = getUserById(userId);
        user.setRole(newRole);
        // Clear engineer-specific fields if not engineer
        if (newRole != Role.ENGINEER) {
            user.setEngineerLevel(null);
            user.setSpecializedCategories(null);
        }
        return userRepository.save(user);
    }

    /**
     * Admin updates engineer level and/or specialized categories.
     */
    public User updateEngineerDetails(Long userId, EngineerLevel level) {
        User user = getUserById(userId);
        if (user.getRole() != Role.ENGINEER) {
            throw new IllegalArgumentException("User is not an ENGINEER");
        }
        user.setEngineerLevel(level);
        return userRepository.save(user);
    }

    /**
     * Admin deletes a user permanently.
     */
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}