package project.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import project.library.entity.Book;
import project.library.entity.Order;
import project.library.entity.Role;
import project.library.entity.User;
import project.library.service.*;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final BookService bookService;
    private final UserService userService;
    private final NotificationService notificationService;

    private final RoleService roleService;

    @Autowired
    public OrderController(OrderService orderService, BookService bookService, UserService userService, NotificationService notificationService, RoleService roleService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.roleService = roleService;
    }

    private List<Order> getallOrders() {
        return orderService.findAll();
    }

    @GetMapping("/viewAllOrders")
    public String viewAllOrders(Model model, @AuthenticationPrincipal UserDetails currentUser) throws IOException {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String role = loggedInUser.getAuthorities().toString();
        String username = loggedInUser.getName();
        User user = userService.findByUsername(username);

        List<Order> allOrders = new ArrayList<>();

        if (role.equals("[SuperAdmin]")) {
            allOrders = orderService.findAllWithBooks();
            allOrders.sort(Comparator.comparing(Order::getDateoforder, Comparator.nullsLast(Comparator.reverseOrder())));
        }

        for (Order order : allOrders) {
            boolean isDue = orderService.isOrderDue(order);
            order.setDue(isDue);
        }
        List<String> notifications = notificationService.checkAllOverdueOrders();

        model.addAttribute("notifications", notifications);
        model.addAttribute("user", user);
        model.addAttribute("records", allOrders.size());
        model.addAttribute("allOrders", allOrders);

        return "allOrders";
    }

    @GetMapping("/myOrders")
    public String viewMyOrders(Model model, @AuthenticationPrincipal UserDetails currentUser) throws IOException {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String role = loggedInUser.getAuthorities().toString();
        String username = loggedInUser.getName();
        User user = userService.findByUsername(username);

        List<Order> userOrders = new ArrayList<>();


            userOrders = orderService.findOrdersByUser(user);
            userOrders.sort(Comparator.comparing(Order::getDateoforder, Comparator.nullsLast(Comparator.reverseOrder())));


        for (Order order : userOrders) {
            boolean isDue = orderService.isOrderDue(order);
            order.setDue(isDue);
        }
        List<String> notifications = notificationService.checkUserOverdueOrders(user);

        model.addAttribute("notifications", notifications);
        model.addAttribute("user", user);
        model.addAttribute("records", userOrders.size());
        model.addAttribute("userOrders", userOrders);

        return "myOrders";
    }


    @PostMapping("/deleteOrder")
    public String deleteOrder(@RequestParam("orderId") Integer orderId) {
        Order order = orderService.findById(orderId);
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String role = loggedInUser.getAuthorities().toString();
        String username = loggedInUser.getName();
        User user = userService.findByUsername(username);

        if (order != null) {
            Book book = order.getBookByBookId();
            if (order.getStatus() == 2 || order.getStatus() == 0) {
                book.setStock(book.getStock() + 1);
                bookService.save(book);
                orderService.deleteOrder(orderId);
            } else {
                orderService.deleteOrder(orderId);
            }
            if (role.contains("SuperAdmin")) {
                return "redirect:/orders/viewAllOrders";
            } else if (role.contains("User")) {
                return "redirect:/orders/myOrders";
            }
        }
        return "redirect:/index";
    }

    @GetMapping("/triggerUpdateDueOrders")
    public String triggerUpdateDueOrders() {
        orderService.updateDueOrders();
        return "redirect:/orders/viewAllOrders";
    }


    @PostMapping("/acceptOrder")
    public String acceptOrder(@RequestParam(value = "orderId", required = true) int orderId, RedirectAttributes attributes) {
        Order order = orderService.findById(orderId);
        if (order != null) {
            Book book = order.getBookByBookId();


            LocalDate currentDate = LocalDate.now();
            LocalDate dateOfReturn = currentDate.plusDays(10);

            // Convert LocalDate to Timestamp
            Timestamp timestampDateOfReturn = Timestamp.valueOf(dateOfReturn.atStartOfDay());

            // Format Timestamp to a desired pattern
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateOfReturn = dateFormat.format(timestampDateOfReturn);

            order.setDateOfReturn(Timestamp.valueOf(formattedDateOfReturn));
            orderService.accept(order);

            bookService.save(book);
            attributes.addFlashAttribute("message", "Order accepted successfully");
        }
        return "redirect:/orders/viewAllOrders";
    }


    @PostMapping("/extendOrder")
    public String extendOrder(@RequestParam(value = "orderId", required = true) int orderId, RedirectAttributes attributes) {
        Order order = orderService.findById(orderId);
        if (order != null && order.getStatus() == 2) {
            orderService.extend(order);

            attributes.addFlashAttribute("message", "Order with ID:" + orderId + " extended for 7 days");
        }
        return "redirect:/orders/viewAllOrders";
    }


    @PostMapping("/denyOrder")
    public String denyOrder(@RequestParam(value = "orderId", required = true) int orderId, RedirectAttributes attributes) {
        Order order = orderService.findById(orderId);
        Book book = order.getBookByBookId();
        if (order != null) {
            book.setStock(book.getStock() + 1);
            orderService.deny(order);

            attributes.addFlashAttribute("message", "Order denied successfully");
        }
        return "redirect:/orders/viewAllOrders";
    }


    @GetMapping("/placeOrder")
    public String placeOrder(@RequestParam("bookId") Integer bookId) {
        // Retrieve the book with the given ID
        Book book = bookService.findById(bookId);

        // Check if the book is available
        if (book != null && book.getStock() > 0) {
            // Create a new order
            Order newOrder = new Order();
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            String username = loggedInUser.getName();
            User user = userService.findByUsername(username);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            newOrder.setStatus(0);
            newOrder.setUserByUserId(user);
            newOrder.setBookByBookId(book);
            newOrder.setDateoforder(currentTimestamp);

            book.setStock(book.getStock() - 1);
            orderService.save(newOrder);


            return "redirect:/books";
        } else {

            return "redirect:/books";
        }
    }

    @PostMapping("/placeOrder")
    public String processOrder(Model model, @RequestParam("bookId") Integer bookId) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String role = loggedInUser.getAuthorities().toString();
        String username = loggedInUser.getName();

        User user = userService.findByUsername(username);
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        Book book = bookService.findById(bookId);

        if (book.getStock() > 0 && user.getEnable() == 1) {
            Order newOrderForm = new Order();
            newOrderForm.setStatus(0);
            newOrderForm.setUserByUserId(user);

            Order obj = getallOrders().stream().max(Comparator.comparing(Order::getId)).orElse(null);
            Integer newId = (Integer) (obj == null ? 1 : (obj.getId() + 1));
            newOrderForm.setId(newId);

            // Set other order details
            newOrderForm.setBookByBookId(book);

            // Set date of order
            newOrderForm.setDateoforder(currentTimestamp);
            book.setStock(book.getStock() - 1);
            // Save the order
            orderService.save(newOrderForm);

            // Add user to the model
            model.addAttribute("user", userService.findByUsername(username));

            if (role.contains("SuperAdmin")) {
                return "redirect:/orders/viewAllOrders";
            } else {
                return "redirect:/orders/myOrders";
            }
        }
        return "redirect:/index";
    }
}
