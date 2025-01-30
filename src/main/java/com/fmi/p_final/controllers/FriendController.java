package com.fmi.p_final.controllers;

import com.fmi.p_final.entities.Friend;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.services.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/add")
    public ResponseEntity<AppResponse<Friend>> addFriend(
            @RequestParam Long userId,
            @RequestParam Long friendId) {
        return friendService.addFriend(userId, friendId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AppResponse<List<Friend>>> getUserFriends(@PathVariable Long userId) {
        return friendService.getUserFriends(userId);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<AppResponse<Object>> removeFriend(
            @RequestParam Long userId,
            @RequestParam Long friendId) {
        return friendService.removeFriend(userId, friendId);
    }
}