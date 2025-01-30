package com.fmi.p_final.dto;


public class MessageDTO {

    public Long senderId;

    public String senderName;
    public Long channelId;
    public String content;

    public static Long No_Channel=-1l;

    public MessageDTO(Long senderId, String senderName, Long channelId, String content) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.channelId = channelId;
        this.content = content;
    }
}
