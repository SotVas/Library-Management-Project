function showNotificationsPopup() {
    // Pass Thymeleaf notifications array to the showNotification function
    var userOrders = /*[[${userOrders}]]*/ [];
    showNotification(userOrders);
}

window.onload = function () {
    updateOverdueOrdersCount();
}

function updateOverdueOrdersCount() {
    var overdueOrdersCount = document.querySelectorAll('.content-container .order-card .due-message').length;
    document.getElementById('overdueOrdersCount').innerText = overdueOrdersCount;
}

function showNotification(userOrders) {
    // Get the modal body element
    var modalBody = document.getElementById('notificationModalBody');

    // Clear existing content in modalBody
    modalBody.innerHTML = '';

    // Check if there are overdue orders
    var overdueOrdersExist = userOrders.some(function (order) {
        return order.due;
    });

    if (overdueOrdersExist) {
        // Iterate over each order in userOrders
        userOrders.forEach(function (order) {
            // Check if the order is due
            if (order.due) {
                // Construct HTML for order details
                var orderDetailsHtml =
                    '<p>Order ID: ' + order.id + '</p>' +
                    '<p>Book Title: ' + order.bookByBookId.title + '</p>' +
                    '<p>Date of Order: ' + formatDate(order.dateoforder) + '</p>' +
                    '<p>Date of Return: ' + formatDate(order.dateOfReturn) + '</p>' +
                    '<p>Status: ' + getStatusText(order.status) + '</p>' +
                    '<hr>'; // Add a horizontal line between orders

                // Append the orderDetailsHtml to modalBody
                modalBody.innerHTML += orderDetailsHtml;
            }
        });
    } else {
        // Display a message if no overdue orders
        modalBody.innerHTML = '<p>No overdue orders.</p>';
    }
}




