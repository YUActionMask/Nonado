package com.example.nonado;

public class Point {
    private String sender;
    private String receiver;
    private int balance;
    private String certification;

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

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
