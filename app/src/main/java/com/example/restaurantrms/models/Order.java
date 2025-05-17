package com.example.restaurantrms.models;

import java.util.List;

public class Order {
    public String orderId;
    public String waiterId;
    public String tableNumber;
    public long orderTime;
    public String status; // received, preparing, ready, served
    public List<OrderItem> items;

    public Order() {}

    public Order(String orderId, String waiterId, String tableNumber, long orderTime, String status, List<OrderItem> items) {
        this.orderId = orderId;
        this.waiterId = waiterId;
        this.tableNumber = tableNumber;
        this.orderTime = orderTime;
        this.status = status;
        this.items = items;
    }
}

