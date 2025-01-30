package com.fmi.p_final.services;

import com.fmi.p_final.entities.User;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<AppResponse<User>> createUser(User user) {
        List<AppResponse.ValidationError> errors = new ArrayList<>();

        if (userRepository.existsByUsername(user.getUsername())) {
            errors.add(new AppResponse.ValidationError("username", "Username is already taken."));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            errors.add(new AppResponse.ValidationError("email", "Email is already registered."));
        }
        if (!errors.isEmpty()) {
            return AppResponse.validationError("User registration failed.", errors);
        }

        userRepository.save(user);
        return AppResponse.success(user, "User registered successfully.");
    }


    public ResponseEntity<AppResponse<User>> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> AppResponse.success(user, "User retrieved successfully."))
                .orElseGet(() -> AppResponse.error(HttpStatus.NOT_FOUND, "User not found."));
    }

    public ResponseEntity<AppResponse<User>> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> AppResponse.success(user, "User found."))
                .orElseGet(() -> AppResponse.error(HttpStatus.NOT_FOUND, "User not found."));
    }

    public ResponseEntity<AppResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findByIsDeletedFalse();
        return AppResponse.success(users, "Active users retrieved successfully.");
    }


    @Transactional
    public ResponseEntity<AppResponse<User>> updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        List<AppResponse.ValidationError> errors = new ArrayList<>();

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().trim().isEmpty()) {
            if (userRepository.existsByUsername(updatedUser.getUsername()) &&
                    !user.getUsername().equals(updatedUser.getUsername())) {
                errors.add(new AppResponse.ValidationError("username", "Username is already taken."));
            } else {
                user.setUsername(updatedUser.getUsername());
            }
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {
            if (userRepository.existsByEmail(updatedUser.getEmail()) &&
                    !user.getEmail().equals(updatedUser.getEmail())) {
                errors.add(new AppResponse.ValidationError("email", "Email is already registered."));
            } else {
                user.setEmail(updatedUser.getEmail());
            }
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
                user.setPassword(updatedUser.getPassword());
        }


        if (!errors.isEmpty()) {
            return AppResponse.validationError("User update failed.", errors);
        }
        userRepository.save(user);
        return AppResponse.success(user, "User updated successfully.");
    }

    @Transactional
    public ResponseEntity<AppResponse<Object>> deleteUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setDeleted(true);
                    userRepository.save(user);
                    return AppResponse.success(null, "User deleted successfully.");
                })
                .orElseGet(() -> AppResponse.error(HttpStatus.NOT_FOUND, "User not found."));
    }
}
