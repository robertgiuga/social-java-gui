package com.example.socialtpygui.domain;


import java.time.LocalDate;
import java.util.List;

public class Message extends Entity<Integer>{
    private String from;
    private List<String> to;
    private String message;
    private LocalDate data;

    public Message(){}
    public Message(String from, List<String> to, String message, LocalDate data) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
    }


    public String getFrom() {return from;}

    public List<String> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getData() {
        return data;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
