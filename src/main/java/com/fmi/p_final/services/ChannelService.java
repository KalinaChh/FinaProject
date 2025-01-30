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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    private final ChannelMemberRepository channelMemberRepository;

    @Transactional
    public ResponseEntity<AppResponse<Channel>> createChannel(Channel channel) {
        User owner = userRepository.findById(channel.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        channel.setOwner(owner);
        channelRepository.save(channel);

        ChannelMember ownerMember = new ChannelMember();
        ownerMember.setUser(owner);
        ownerMember.setChannel(channel);
        ownerMember.setRole(ChannelMember.Role.OWNER);

        channelMemberRepository.save(ownerMember);

        return AppResponse.success(channel, "Channel created successfully, and owner added as ADMIN.");
    }


    public ResponseEntity<AppResponse<List<Channel>>> getAllChannels() {
        return AppResponse.success(channelRepository.findAll(), "All channels retrieved successfully.");
    }

    public ResponseEntity<AppResponse<Channel>> getChannelById(Long channelId) {
        return channelRepository.findById(channelId)
                .map(channel -> AppResponse.success(channel, "Channel retrieved successfully."))
                .orElseGet(() -> AppResponse.error(HttpStatus.NOT_FOUND, "Channel not found."));
    }

    @Transactional
    public ResponseEntity<AppResponse<Channel>> updateChannelName(Long channelId, String newName, MockedSecurity ms) {
        Channel channel = channelRepository.findById(channelId)
                .orElse(null);

        if (channel == null) {
            return AppResponse.error(HttpStatus.NOT_FOUND, "Channel not found.");
        }

        if (!MockedSecutiryController.isAdmin(ms)) {
            return AppResponse.error(HttpStatus.FORBIDDEN, "Only an ADMIN can rename the channel.");
        }

        if (channelRepository.existsByName(newName)) {
            return AppResponse.error(HttpStatus.BAD_REQUEST, "Channel name is already taken.");
        }

        channel.setName(newName);
        channelRepository.save(channel);
        return AppResponse.success(channel, "Channel name updated successfully.");
    }

    @Transactional
    public ResponseEntity<AppResponse<Object>> deleteChannel(Long channelId, MockedSecurity ms) {
        return channelRepository.findById(channelId)
                .map(channel -> {

                    if (!channel.getOwner().getId().equals(ms.requesterId) && !MockedSecutiryController.isOwner(ms)) {
                        return AppResponse.error(HttpStatus.FORBIDDEN, "Only the channel OWNER can delete the channel.");
                    }

                    channel.setDeleted(true);
                    channelRepository.save(channel);

                    return AppResponse.success(null, "Channel deleted successfully.");
                })
                .orElseGet(() -> AppResponse.error(HttpStatus.NOT_FOUND, "Channel not found."));
    }
}
