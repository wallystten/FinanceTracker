package com.finance.tracker;

public class Transaction {

    public long id;
    public String description;
    public double value;
    public String type;
    public String category;
    public long timestamp;

    public Transaction() {
        this.timestamp = System.currentTimeMillis();
    }

    public Transaction(String description, double value, String type, String category) {
        this.description = description;
        this.value = value;
        this.type = type;
        this.category = category;
        this.timestamp = System.currentTimeMillis();
    }
}
