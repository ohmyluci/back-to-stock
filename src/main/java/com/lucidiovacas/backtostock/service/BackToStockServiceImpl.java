package com.lucidiovacas.backtostock.service;

import com.lucidiovacas.backtostock.model.Product;
import com.lucidiovacas.backtostock.model.User;
import static com.lucidiovacas.backtostock.constants.Constants.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BackToStockServiceImpl implements BackToStockService{

    @Override
    public void subscribe(User user, Product product) {
        product.incrementSubscribedUsers();
        switch(product.getCategory()){
            case MEDICAL:
                subscribeUserToMedicalProduct(product, user);
                break;
            default:
                subscribeUserToNonMedicalProduct(product, user);
                break;
        }
    }

    @Override
    public List<User> subscribedUsers(Product product) {
        List<User> subscribedUser = new ArrayList<>();
        subscribedUser.addAll((List<User>) product.getHighPrioritySubscribers());
        subscribedUser.addAll((List<User>) product.getMediumPrioritySubscribers());
        subscribedUser.addAll((List<User>) product.getLowPrioritySubscribers());
        return subscribedUser;
    }

    @Override
    public void productBackToStock(Product product, Long newStock) {
        product.setStock(newStock);
    }

    @Override
    public List<User> notifySubscribedUsers(Product product) {
        Long remainingStock = product.getStock();
        List<User> subscribedUsers = subscribedUsers(product);
        List<User> usersNotified = new ArrayList<>();

        while(remainingStock > 0 && subscribedUsers.size() > 0) {
            User userToNotify = subscribedUsers.get(0);
            System.out.println(notifyUser(userToNotify, product));
            removeSubscribedUserFromQueue(product, userToNotify);
            remainingStock--;
            subscribedUsers = subscribedUsers(product);
            usersNotified.add(userToNotify);
        }

        return usersNotified;
    }

    public void removeSubscribedUserFromQueue(Product product, User user) {
        switch(product.getCategory()){
            case MEDICAL:
                unsubscribeUserForMedicalProduct(product, user);
                break;
            default:
                unsubscribeUserForNonMedicalProduct(product, user);
                break;
        }
    }

    public void subscribeUserToMedicalProduct(Product product, User user) {
        switch (user.getPriorityForMedicalProduct()) {
            case HIGH_PRIORITY:
                product.getHighPrioritySubscribers().add(user);
                break;
            case LOW_PRIORITY:
                product.getLowPrioritySubscribers().add(user);
                break;
            default:
                break;
        }
    }

    public void subscribeUserToNonMedicalProduct(Product product, User user) {
        switch (user.getPriorityForNonMedicalProduct()) {
            case HIGH_PRIORITY:
                product.getHighPrioritySubscribers().add(user);
                break;
            case MEDIUM_PRIORITY:
                product.getMediumPrioritySubscribers().add(user);
                break;
            case LOW_PRIORITY:
                product.getLowPrioritySubscribers().add(user);
                break;
            default:
                break;
        }
    }

    public void unsubscribeUserForMedicalProduct(Product product, User user) {
        switch (user.getPriorityForMedicalProduct()) {
            case HIGH_PRIORITY:
                product.getHighPrioritySubscribers().poll();
                break;
            case LOW_PRIORITY:
                product.getLowPrioritySubscribers().poll();
                break;
            default:
                break;
        }
    }

    public void unsubscribeUserForNonMedicalProduct(Product product, User user) {
        switch (user.getPriorityForNonMedicalProduct()) {
            case HIGH_PRIORITY:
                product.getHighPrioritySubscribers().poll();
                break;
            case MEDIUM_PRIORITY:
                product.getMediumPrioritySubscribers().poll();
                break;
            case LOW_PRIORITY:
                product.getLowPrioritySubscribers().poll();
                break;
            default:
                break;
        }
    }

    public String notifyUser(User userToNotify, Product product) {
        return
            String.format("User %s has been notified that product with id %s is back to stock",
            userToNotify.getName(), product.getId());
    }
}
