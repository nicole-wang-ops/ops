package com.example.demo12.entity;

import lombok.Data;

import java.util.List;

@Data
public class Message2 extends Message {
    public String adavatar;

    public Message parentMessage;

    public List<Message2> sonMessages;

//    public void setParentMessage(Message2 message) {
//        this.parentMessage = message;
//    }

}
