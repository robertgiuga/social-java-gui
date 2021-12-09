package com.example.socialtpygui.domain;

import java.time.LocalDate;
import java.util.List;

public class ReplyMessage extends Message{
    Message original;

    public ReplyMessage(Message response ,Message original){
        super(response.getFrom(),response.getTo(),response.getMessage(),response.getData());
        super.setId(response.getId());

        this.original =original;
    }

    public ReplyMessage(String from, List<String> to, String message, LocalDate data, Message original) {
        super(from, to, message, data);
        this.original = original;
    }


    public Message getOriginal() {
        return original;
    }

    public void setOriginal(Message original) {
        this.original = original;
    }
}
