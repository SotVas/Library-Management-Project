package project.library.service;


import project.library.entity.User;

import java.util.List;

public interface NotificationService {


   List<String> checkAllOverdueOrders();

List<String> checkUserOverdueOrders(User user);
}
