package com.example.restaurantrms.models;

public class OrderItem {
    public String itemId;
    public int quantity;
    public String notes;

    public OrderItem() {}

    public OrderItem(String itemId, int quantity, String notes) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.notes = notes;
    }
}
