package com.example.a7sticker;

import java.util.Date;

public class Message {

    public String messageId;
    public String sender;
    public String receiver;
    public Date timeStamp;
    public Integer stickerId;

    public Message() {
    }


    public Message(String messageId, String sender, String receiver, Date timeStamp, Integer stickerId) {
        this.messageId = messageId;
        this.sender = sender;
        this.receiver = receiver;
        this.timeStamp = timeStamp;
        this.stickerId = stickerId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Integer getStickerId() {
        return stickerId;
    }

}
