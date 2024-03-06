package project.library.service.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.library.entity.Order;
import project.library.entity.User;
import project.library.repository.OrderRepository;
import project.library.service.NotificationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private OrderRepository orderRepository;

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String OVERDUE_NOTIFICATION_FORMAT = "Order with ID %d is %s. Return date: %s";

    public List<String> checkAllOverdueOrders() {
        List<Order> overdueOrders = orderRepository.findOverdueOrders();
        return formatNotifications(overdueOrders);
    }

    public List<String> checkUserOverdueOrders(User user) {
        List<Order> userOverdueOrders = orderRepository.findUserOverdueOrders(user);
        return formatNotifications(userOverdueOrders);
    }

    private List<String> formatNotifications(List<Order> orders) {
        List<String> notifications = new ArrayList<>();
        for (Order order : orders) {
            try {
                String notification = formatNotification(order);
                notifications.add(notification);
            } catch (Exception e) {

            }
        }
        return notifications;
    }

    private String formatNotification(Order order) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String formattedDate = dateFormat.format(order.getDateOfReturn());
        String status = order.isDue() ? "overdue" : "not yet due";
        return String.format(OVERDUE_NOTIFICATION_FORMAT, order.getId(), status, formattedDate);
    }
}