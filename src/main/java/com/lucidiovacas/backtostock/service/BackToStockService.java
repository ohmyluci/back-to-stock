package com.lucidiovacas.backtostock.service;

import com.lucidiovacas.backtostock.model.Product;
import com.lucidiovacas.backtostock.model.User;

import java.util.List;

public interface BackToStockService {

    void subscribe(User user, Product product);

    List<User> subscribedUsers(Product product);

    void productBackToStock(Product product, Long newStock);

    List<User> notifySubscribedUsers(Product product);
}
