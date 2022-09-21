package com.lucidiovacas.backtostock.service;

import com.lucidiovacas.backtostock.model.Product;
import com.lucidiovacas.backtostock.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.lucidiovacas.backtostock.model.ProductCategory.BOOKS;
import static com.lucidiovacas.backtostock.model.ProductCategory.MEDICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BackToStockServiceImplTest {

    @Autowired
    private BackToStockService backToStockService;

    @Test
    void checkOrderOfNotificationsForMedicalProduct() {
        // Create 6 users, premium, not premium, elderly, and not elderly
        User nonPremiumUser55Years  = new User("nonPremiumUser55years", Boolean.FALSE, 55);
        User premiumUser40Years     = new User("premiumUser40Years", Boolean.TRUE, 40);
        User nonPremiumUser72Years  = new User("nonPremiumUser72Years", Boolean.FALSE, 72);
        User nonPremiumUser57Years  = new User("nonPremiumUser57Years", Boolean.FALSE, 57);
        User premiumUser82Years     = new User("premiumUser82Years", Boolean.TRUE, 82);
        User premiumUser83Years     = new User("premiumUser82Years2", Boolean.TRUE, 82);

        // Create a medical product with stock 0 units
        Product medicalProduct1 = new Product("medicalProduct1", MEDICAL, 0L, 0L);

        // Subscribe the 6 users to the medical product in the following order
        backToStockService.subscribe(nonPremiumUser55Years, medicalProduct1);
        backToStockService.subscribe(premiumUser40Years, medicalProduct1);
        backToStockService.subscribe(nonPremiumUser72Years, medicalProduct1);
        backToStockService.subscribe(nonPremiumUser57Years, medicalProduct1);
        backToStockService.subscribe(premiumUser82Years, medicalProduct1);
        backToStockService.subscribe(premiumUser83Years, medicalProduct1);

        assertEquals(6L, medicalProduct1.getSubscribedUsers(),
                "Number of subscribed users must be 6");


        // Make the product back to stock with 3 new units
        backToStockService.productBackToStock(medicalProduct1, 3L);

        // Notify the 3 users that product is again in stock
        List<User> notifiedUsers = backToStockService.notifySubscribedUsers(medicalProduct1);

        // Check that the users are notified depending on the order but also their priority
        assertEquals(3L, notifiedUsers.size());
        assertEquals(premiumUser40Years, notifiedUsers.get(0));
        assertEquals(nonPremiumUser72Years, notifiedUsers.get(1));
        assertEquals(premiumUser82Years, notifiedUsers.get(2));
    }

    @Test
    void checkOrderOfNotificationsForNonMedicalProduct() {
        // Create 5 users, premium, not premium, elderly, and not elderly
        User nonPremiumUser55Years  = new User("nonPremiumUser55years", Boolean.FALSE, 55);
        User premiumUser40Years     = new User("premiumUser40Years", Boolean.TRUE, 40);
        User nonPremiumUser72Years  = new User("nonPremiumUser72Years", Boolean.FALSE, 72);
        User nonPremiumUser57Years  = new User("nonPremiumUser57Years", Boolean.FALSE, 57);
        User premiumUser82Years     = new User("premiumUser82Years", Boolean.TRUE, 82);

        // Create a non medical product with stock 0 units
        Product bookProduct1 = new Product("bookProduct1", BOOKS, 0L, 0L);

        // Subscribe the 5 users to the book product in the following order
        backToStockService.subscribe(nonPremiumUser55Years, bookProduct1);
        backToStockService.subscribe(premiumUser40Years, bookProduct1);
        backToStockService.subscribe(nonPremiumUser72Years, bookProduct1);
        backToStockService.subscribe(nonPremiumUser57Years, bookProduct1);
        backToStockService.subscribe(premiumUser82Years, bookProduct1);

        assertEquals(5L, bookProduct1.getSubscribedUsers(),
                "Number of subscribed users must be 5");


        // Make the product back to stock with 4 new units
        backToStockService.productBackToStock(bookProduct1, 4L);

        // Notify the 4 users that product is again in stock
        List<User> notifiedUsers = backToStockService.notifySubscribedUsers(bookProduct1);

        // Check that the users are notified depending on the order but also their priority
        assertEquals(4L, notifiedUsers.size());
        assertEquals(premiumUser40Years, notifiedUsers.get(0));
        assertEquals(premiumUser82Years, notifiedUsers.get(1));
        assertEquals(nonPremiumUser72Years, notifiedUsers.get(2));
        assertEquals(nonPremiumUser55Years, notifiedUsers.get(3));
    }


    @Test
    void subscribe() {
        User nonPremiumUser55Years  = new User("nonPremiumUser55years", Boolean.FALSE, 55);
        User premiumUser40Years     = new User("premiumUser40Years", Boolean.TRUE, 40);
        Product medicalProduct = new Product("newMedicalProduct", MEDICAL, 0L, 0L);

        assertEquals(0L, medicalProduct.getSubscribedUsers());

        backToStockService.subscribe(nonPremiumUser55Years, medicalProduct);
        assertEquals(1L, medicalProduct.getSubscribedUsers());

        backToStockService.subscribe(premiumUser40Years, medicalProduct);
        assertEquals(2L, medicalProduct.getSubscribedUsers());
    }

    @Test
    void subscribedUsers() {
        User nonPremiumUser55Years  = new User("nonPremiumUser55years", Boolean.FALSE, 55);
        User premiumUser40Years     = new User("premiumUser40Years", Boolean.TRUE, 40);
        Product medicalProduct = new Product("newMedicalProduct", MEDICAL, 0L, 0L);
        backToStockService.subscribe(nonPremiumUser55Years, medicalProduct);
        backToStockService.subscribe(premiumUser40Years, medicalProduct);

        List<User> usersSubscribedByPriorityOrder = backToStockService.subscribedUsers(medicalProduct);

        List expectedSubscribersInOrder = new ArrayList<>(List.of(premiumUser40Years, nonPremiumUser55Years));
        assertEquals(expectedSubscribersInOrder, usersSubscribedByPriorityOrder);

    }

    @Test
    void productBackToStock() {
        Product medicalProduct = new Product("newMedicalProduct", MEDICAL, 0L, 0L);

        assertEquals(0L, medicalProduct.getStock());

        backToStockService.productBackToStock(medicalProduct, 2L);

        assertEquals(2L, medicalProduct.getStock());

    }

    @Test
    void notifySubscribedUsers() {
        User nonPremiumUser55Years  = new User("nonPremiumUser55years", Boolean.FALSE, 55);
        User premiumUser40Years     = new User("premiumUser40Years", Boolean.TRUE, 40);
        Product medicalProduct = new Product("newMedicalProduct", MEDICAL, 0L, 0L);
        backToStockService.subscribe(nonPremiumUser55Years, medicalProduct);
        backToStockService.subscribe(premiumUser40Years, medicalProduct);

        backToStockService.productBackToStock(medicalProduct, 2L);
        List<User> usersNotified = backToStockService.notifySubscribedUsers(medicalProduct);

        List<User> usersNotifiedExpected = new ArrayList<>(List.of(premiumUser40Years, nonPremiumUser55Years));
        assertEquals(usersNotifiedExpected, usersNotified);
    }



    @Test
    void removeSubscribedUserFromQueue() {
    }

    @Test
    void subscribeUserToMedicalProduct() {
    }

    @Test
    void subscribeUserToNonMedicalProduct() {
    }

    @Test
    void unsubscribeUserForMedicalProduct() {
    }

    @Test
    void unsubscribeUserForNonMedicalProduct() {
    }

    @Test
    void notifyUser() {
    }
}