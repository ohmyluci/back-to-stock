package com.lucidiovacas.backtostock.model;

import lombok.Data;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;

@Data
public class Product {

    private final String id;
    private final ProductCategory category;
    private Long stock;
    private Queue<User> highPrioritySubscribers;
    private Queue<User> mediumPrioritySubscribers;
    private Queue<User> lowPrioritySubscribers;
    private Long subscribedUsers = 0L;


    public Product(String id, ProductCategory category, Long stock, Long subscribedUsers) {
        this.id = id;
        this.category = category;
        this.stock = stock;
        this.highPrioritySubscribers = new LinkedList<>();
        this.mediumPrioritySubscribers = new LinkedList<>();
        this.lowPrioritySubscribers = new LinkedList<>();
        this.subscribedUsers = subscribedUsers;
    }

    public void incrementSubscribedUsers() {
        this.subscribedUsers += 1;
    }

}
