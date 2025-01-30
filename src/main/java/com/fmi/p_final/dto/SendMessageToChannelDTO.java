package com.fmi.p_final.dto;

import com.fmi.p_final.entities.User;

public class SendMessageToChannelDTO {
    public SenderDTO sender;
    public Long channelId;
    public String content;

    public SendMessageToChannelDTO(SenderDTO sender, Long channelId, String content) {
        this.sender = sender;
        this.channelId = channelId;
        this.content = content;
    }
}
