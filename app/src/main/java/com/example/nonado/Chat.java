package com.example.nonado;

public class Chat {
    private String sender;
    private String receiver;
    private String msg;

    public String getName() {
        return sender;
    }

    public void setName(String name) {
        this.sender = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
