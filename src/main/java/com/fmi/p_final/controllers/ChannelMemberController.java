package com.fmi.p_final.controllers;

import com.fmi.p_final.entities.ChannelMember;
import com.fmi.p_final.entities.MockedSecurity;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.services.ChannelMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/channel-members")
@RequiredArgsConstructor
public class ChannelMemberController {

    private final ChannelMemberService channelMemberService;

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<AppResponse<List<ChannelMember>>> getChannelMembers(@PathVariable Long channelId) {
        return channelMemberService.getChannelMembers(channelId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AppResponse<List<ChannelMember>>> getUserChannels(@PathVariable Long userId) {
        return channelMemberService.getChannelsByUser(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<AppResponse<ChannelMember>> addUserToChannel(
            @Valid @RequestBody ChannelMember channelMember,
            @RequestHeader("Mocked-Token") String mockedToken,
            @RequestHeader("Mocked-RequesterId") Long mockedRequesterId) {

        MockedSecurity ms = new MockedSecurity();
        ms.token = mockedToken;
        ms.requesterId = mockedRequesterId;

        return channelMemberService.addUserToChannel(channelMember, ms);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<AppResponse<Object>> removeUserFromChannel(
            @RequestParam Long userId,
            @RequestParam Long channelId,
            @RequestParam String mockedToken,
            @RequestParam Long mockedRequesterId) {

        MockedSecurity ms = new MockedSecurity();
        ms.token = mockedToken;
        ms.requesterId = mockedRequesterId;

        return channelMemberService.removeUserFromChannel(userId, channelId, ms);
    }


    @PutMapping("/update-role")
    public ResponseEntity<AppResponse<ChannelMember>> updateUserRole(
            @RequestParam Long userId,
            @RequestParam Long channelId,
            @RequestParam ChannelMember.Role newRole,
            @RequestParam String mockedToken,
            @RequestParam Long mockedRequesterId) {

        MockedSecurity ms = new MockedSecurity();
        ms.token = mockedToken;
        ms.requesterId = mockedRequesterId;

        return channelMemberService.updateUserRole(userId, channelId, newRole, ms);
    }
}
