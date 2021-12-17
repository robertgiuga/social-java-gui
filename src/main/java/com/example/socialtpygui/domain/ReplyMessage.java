package com.example.socialtpygui.domain;

import java.time.LocalDate;
import java.util.List;

public class ReplyMessage extends MessageDTO {
    MessageDTO original;

    public ReplyMessage(MessageDTO response , MessageDTO original){
        super(response.getFrom(),response.getTo(),response.getMessage(),response.getData());
        super.setId(response.getId());

        this.original =original;
    }

    public ReplyMessage(String from, List<String> to, String message, LocalDate data, MessageDTO original) {
        super(from, to, message, data);
        this.original = original;
    }


    public MessageDTO getOriginal() {
        return original;
    }

    public void setOriginal(MessageDTO original) {
        this.original = original;
    }
}
