package com.fmi.p_final.controllers;

import com.fmi.p_final.entities.Channel;
import com.fmi.p_final.entities.MockedSecurity;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.services.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<AppResponse<Channel>> createChannel(@Valid @RequestBody Channel channel) {
        return channelService.createChannel(channel);
    }


    @GetMapping
    public ResponseEntity<AppResponse<List<Channel>>> getAllChannels() {
        return channelService.getAllChannels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<Channel>> getChannelById(@PathVariable Long id) {
        return channelService.getChannelById(id);
    }

    @PutMapping("/{channelId}")
    public ResponseEntity<AppResponse<Channel>> updateChannelName(
            @PathVariable Long channelId,
            @RequestParam String newName,
            @RequestHeader("Mocked-Token") String mockedToken,
            @RequestHeader("Mocked-RequesterId") Long mockedRequesterId
    ) {
        MockedSecurity ms = new MockedSecurity();
        ms.token = mockedToken;
        ms.requesterId = mockedRequesterId;

        return channelService.updateChannelName(channelId, newName, ms);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<AppResponse<Object>> deleteChannel(
            @PathVariable Long channelId,
            @RequestHeader("Mocked Token") String token,
            @RequestHeader("Mocked Requester Id") Long requesterId
    ) {
        MockedSecurity ms = new MockedSecurity();
        ms.token = token;
        ms.requesterId = requesterId;

        return channelService.deleteChannel(channelId, ms);
    }
}
