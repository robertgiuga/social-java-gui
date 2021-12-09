package com.example.socialtpygui.domain;


public class ReplyMessageDTO {
    private Message response;
    private String originalId;

    public ReplyMessageDTO(Message response, String originalId) {
        this.response = response;
        this.originalId = originalId;
    }

    public Message getResponse() {
        return response;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setResponse(Message response) {
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
