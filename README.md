# Back-To-Stock application

## Introduction
This project has been carried out as a task to allow our customer AmazingCo to let users to subscribe to products that are out of stock. The idea is to notify customer when the product is back in stock.

AmazingCo has already in place premium users and also some users may have priority for particular product categories.

- Premium user has a high priority
- Users elder than 70 years old has:
  - High priority for medical products
  - Medium priority for all categories
- FIFO for all the rest

## Information about solution delivered
First, I want to make clear that I am not completely happy with the solution I have got. 

I tried to use some kind of priority queue where I could have all the subscribers in the same queue and reorder the queue when a new subscriber gets in. I used a queue and a comparable, having into account the different priorities a user could have to reorder the queue with the order of arrival and also the priority. But it wasn't working properly and I lost lot of time meesy around with this algorithm.

Although I don't like the idea of having 3 different queues, it is the solution I am presenting. I have created a high_priority_queue, a medium_priority_queue and a low_priority_queue. 
I am calculating the priority depending on two different scenarios.
- For medical products we have 2 priorities:
  - High priority: for premium users or elderly people
  - Low priority: for rest of users
- For non medical products we have 3 priorities:
  - High priority: for premium users
  - Medium priority: for elderly people
  - Low priority: for rest of users

This way, the priority of a user can be calculated having into account if it is premium, elderly, or none of them. When a user wants to subscribe to a product, we calculate his priority and we added to the queue corresponding to that priority.

When we want to retrieve all the users subscribed to a product (in order, having priorities into account), we get a list that includes the different queues, returning first the high priority queue, follow by the medium priority queue and ending with the low priority queue. For medical products, medium priority queue will be always empty.

## Structure

### com.lucidiovacas.backtostock.model
The different entities used in the project can be found here:
- Product
- ProductCategory
- User

### com.lucidiovacas.backtostock.service
The BackToStockService interface an its implementation. There are also some helpful methods used by the main methods:

- void subscribe(User user, Product product)
  - Depending on the type of product, subscribes the user to the product queue corresponding to the user priority
  
- List<User> subscribedUsers(Product product)
  - Returns a list with all the users subscribed to a product order by their priority and their order when the priority is the same

- void ProductBackToStock(Product product, Long newStock)
  - Easy method that only sets the new stock for a product

- List<User> notifySubscribedUsers(Product product)
  - Depending on the new stock and the number of subscribed users, it will notify, in order, all the possible users while there is still remaining stock or remaining subscribed users without being notified. This method notify the user with highest priority, remove it from the corresponding queue and repeat the process. For the task purpose, the notification is just a log in the console with the next format "User premiumUser40Years has been notified that product with id newMedicalProduct is back to stock"

### Tests is com.lucidiovacas.backtostock.service
The main functionality of the task has been tested for different types of products, medical and not medical ones, subscribing users with different priorities and checking that the users are subscribed and notify depending on this priorities.

Apart from main methods for the service class, helpers methods of the service class have been also tested.
