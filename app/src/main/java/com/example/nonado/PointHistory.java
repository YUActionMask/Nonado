package com.example.nonado;

public class PointHistory {
    private String type;
    private String pointName;
    private int amount;

    public PointHistory(String  type, String pointName, int amount){
        this.type = type;
        this.pointName = pointName;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getPointName() {
        return pointName;
    }

    public String getType() {
        return type;
    }
}
