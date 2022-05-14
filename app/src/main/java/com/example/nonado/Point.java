package com.example.nonado;

public class Point {
    String type;
    String pointName;
    Integer amount;
    public Point(String type, String pointName, int pointAmount){
        this.type = type;
        this.pointName = pointName;
        this.amount = pointAmount;
    }

    public String getPointName() {
        return pointName;
    }

    public String getType() {
        return type;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }
}
