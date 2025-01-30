package com.fmi.p_final.services;

import com.fmi.p_final.controllers.MockedSecutiryController;
import com.fmi.p_final.entities.Channel;
import com.fmi.p_final.entities.ChannelMember;
import com.fmi.p_final.entities.MockedSecurity;
import com.fmi.p_final.entities.User;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.repositories.ChannelMemberRepository;
import com.fmi.p_final.repositories.ChannelRepository;
import com.fmi.p_final.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelMemberService {

    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    private Channel getChannel(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));
    }

    @Transactional
    public ResponseEntity<AppResponse<ChannelMember>> addUserToChannel(ChannelMember channelMember, MockedSecurity ms) {
        User requester = userRepository.findById(ms.requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found."));

        User userToAdd = userRepository.findById(channelMember.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        Channel channel = channelRepository.findById(channelMember.getChannel().getId())
                .orElseThrow(() -> new RuntimeException("Channel not found."));


        ChannelMember requesterMember = channelMemberRepository.findByChannelAndUser(channel, requester)
                .orElseThrow(() -> new RuntimeException("Requester is not a member of this channel."));


        boolean isAdmin = requesterMember.getRole() == ChannelMember.Role.ADMIN || MockedSecutiryController.isAdmin(ms);

        if (!isAdmin) {
            return AppResponse.error(HttpStatus.FORBIDDEN, "Only an ADMIN can add members to the channel.");
        }

        if (channelMemberRepository.existsByChannelAndUser(channel, userToAdd)) {
            return AppResponse.error(HttpStatus.BAD_REQUEST, "User is already a member of this channel.");
        }

        ChannelMember newMember = new ChannelMember();
        newMember.setUser(userToAdd);
        newMember.setChannel(channel);
        newMember.setRole(ChannelMember.Role.GUEST);

        channelMemberRepository.save(newMember);
        return AppResponse.success(newMember, "User added to channel.");
    }




    @Transactional
    public ResponseEntity<AppResponse<Object>> removeUserFromChannel(Long userId, Long channelId, MockedSecurity ms) {
        User requester = userRepository.findById(ms.requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found."));

        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        if (!channel.getOwner().getId().equals(ms.requesterId) && !MockedSecutiryController.isOwner(ms)) {
            return AppResponse.error(HttpStatus.FORBIDDEN, "Only the channel OWNER can remove users.");
        }

        if (!channelMemberRepository.existsByChannelAndUser(channel, userToRemove)) {
            return AppResponse.error(HttpStatus.BAD_REQUEST, "User is not a member of this channel.");
        }

        channelMemberRepository.deleteByChannelAndUser(channel, userToRemove);
        return AppResponse.success(null, "User removed from channel.");
    }

    public ResponseEntity<AppResponse<List<ChannelMember>>> getChannelMembers(Long channelId) {
        Channel channel = getChannel(channelId);
        List<ChannelMember> members = channelMemberRepository.findByChannel(channel);

        if (members.isEmpty()) {
            return AppResponse.error(HttpStatus.NOT_FOUND, "No members found for this channel.");
        }

        return AppResponse.success(members, "Channel members retrieved successfully.");
    }

    public ResponseEntity<AppResponse<List<ChannelMember>>> getChannelsByUser(Long userId) {
        User user = getUser(userId);
        List<ChannelMember> channels = channelMemberRepository.findByUser(user);

        if (channels.isEmpty()) {
            return AppResponse.error(HttpStatus.NOT_FOUND, "User is not a member of any channels.");
        }

        return AppResponse.success(channels, "User's channels retrieved successfully.");
    }

    @Transactional
    public ResponseEntity<AppResponse<ChannelMember>> updateUserRole(Long userId, Long channelId, ChannelMember.Role newRole, MockedSecurity ms) {
        User requester = userRepository.findById(ms.requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found."));

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        ChannelMember requesterMember = channelMemberRepository.findByChannelAndUser(channel, requester)
                .orElseThrow(() -> new RuntimeException("Requester is not a member of this channel."));

        boolean isAdmin = requesterMember.getRole() == ChannelMember.Role.ADMIN || MockedSecutiryController.isAdmin(ms);

        if (!isAdmin) {
            return AppResponse.error(HttpStatus.FORBIDDEN, "Only an ADMIN can update user roles in this channel.");
        }

        ChannelMember memberToUpdate = channelMemberRepository.findByChannelAndUser(channel, userToUpdate)
                .orElseThrow(() -> new RuntimeException("User is not a member of this channel."));

        memberToUpdate.setRole(newRole);
        channelMemberRepository.save(memberToUpdate);

        return AppResponse.success(memberToUpdate, "User role updated successfully.");
    }

}
