package project.library.service;

import project.library.entity.Order;
import project.library.entity.User;

import java.util.List;

public interface OrderService {

    Order save(Order order);


    Order findById(int id);

    List<Order> findAll();

    void deny(Order order);

    void accept(Order order);
    void deleteOrdersByUser(User user);
    void deleteOrder(Integer orderId);

    boolean isOrderDue(Order order);

    void updateDueOrders();

    void extend(Order order);

    List<Order> findOrdersByUser(User user);
    List<Order> findAllWithBooks();
}
