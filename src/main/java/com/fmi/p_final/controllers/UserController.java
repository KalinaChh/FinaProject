package com.fmi.p_final.controllers;

import com.fmi.p_final.entities.User;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;



import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<User>> createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<User>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @GetMapping("/search")
    public ResponseEntity<AppResponse<User>> getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<User>>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AppResponse<User>> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<Object>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
