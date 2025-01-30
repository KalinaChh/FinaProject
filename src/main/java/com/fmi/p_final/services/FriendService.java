package com.fmi.p_final.services;

import com.fmi.p_final.entities.Friend;
import com.fmi.p_final.entities.User;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.repositories.FriendRepository;
import com.fmi.p_final.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    @Transactional
    public ResponseEntity<AppResponse<Friend>> addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            return AppResponse.error(HttpStatus.BAD_REQUEST, "You cannot add yourself as a friend.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found."));

        if (friendRepository.existsByUserAndFriend(user, friend)) {
            return AppResponse.error(HttpStatus.BAD_REQUEST, "You are already friends.");
        }

        Friend friendship = Friend.builder()
                .user(user)
                .friend(friend)
                .build();

        return AppResponse.success(friendRepository.save(friendship), "Friend added successfully.");
    }

    public ResponseEntity<AppResponse<List<Friend>>> getUserFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return AppResponse.success(friendRepository.findByUser(user), "Friends retrieved successfully.");
    }

    @Transactional
    public ResponseEntity<AppResponse<Object>> removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found."));

        if (!friendRepository.existsByUserAndFriend(user, friend)) {
            return AppResponse.error(HttpStatus.BAD_REQUEST, "You are not friends.");
        }

        friendRepository.deleteByUserAndFriend(user, friend);
        return AppResponse.success(null, "Friend removed successfully.");
    }
}
