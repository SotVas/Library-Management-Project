package project.library.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.library.entity.Order;
import project.library.entity.User;
import project.library.helper.OrderNotFoundException;
import project.library.repository.BookRepository;
import project.library.repository.OrderRepository;
import project.library.service.OrderService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;

    }
    @Override
    @Transactional
    @Scheduled(fixedRate = 1000)
    public void updateDueOrders() {
        List<Order> allOrders = orderRepository.findAll();

        allOrders.forEach(order -> {
            boolean isDue = isOrderDue(order);
            order.setDue(isDue);
        });

        orderRepository.saveAll(allOrders);
    }

    public boolean isOrderDue(Order order) {
        LocalDateTime currentDate = LocalDateTime.now();
        Boolean isDue = currentDate.isAfter(order.getDateOfReturn().toLocalDateTime());


        if (isDue != order.isDue()) {
            order.setDue(isDue);
            orderRepository.save(order);
        }

        return isDue;
    }
    @Override
    public List<Order> findAllWithBooks() {
        return orderRepository.findAllWithBooks();
    }
    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    @Override
    public void deleteOrder(Integer orderId) {

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {

            orderRepository.deleteById(orderId);
        } else {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
    }

    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByUserByUserId(user);
    }


    public void deleteOrdersByUser(User user) {

        List<Order> ordersToDelete = orderRepository.findByUserByUserId(user);
        for (Order order : ordersToDelete) {
            orderRepository.delete(order);
        }
    }

    @Override
    public Order findById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public void deny(Order order) {
        order.setStatus(1);  // απόρριψη παραγγελίας
        orderRepository.save(order);
    }

       @Override
       @Transactional
       public void extend(Order order) {
       //Παίρνω το υπαρχον return date
        Timestamp DoR =order.getDateOfReturn();

        // to μετατρέπω σε LocalDateTime
        LocalDateTime localDateTime = DoR.toLocalDateTime();

           // Προσθέτω 7 μέρες
           LocalDateTime newLocalDateTime = localDateTime.plusDays(7);

           // το ξαναγυρίζω σε Timestamp
           Timestamp dateOfReturn = Timestamp.valueOf(newLocalDateTime);

           order.setDateOfReturn(dateOfReturn);
           orderRepository.save(order);
    }

    @Override
    public void accept(Order order) {
        order.setStatus(2);
        orderRepository.save(order);
    }
}
