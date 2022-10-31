package com.example.nonado;

public class Point {
    private String sender;
    private String receiver;
    private int balance;
    private String certification;
    private String title;

    public void setBalance(int balance) {this.balance = balance;}

    public void setTitle(String title){this.title = title;}

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle(){return title;}

    public int getBalance() {
        return balance;
    }

    public String getCertification() {
        return certification;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }
}
