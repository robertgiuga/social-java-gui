package com.example.socialtpygui.domain;


public class ReplyMessageDTO {
    private MessageDTO response;
    private String originalId;

    public ReplyMessageDTO(MessageDTO response, String originalId) {
        this.response = response;
        this.originalId = originalId;
    }

    public MessageDTO getResponse() {
        return response;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setResponse(MessageDTO response) {
        this.response = response;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    @Override
    public String toString() {
        return response.toString()+ " replayed to "+ originalId;
    }
}
