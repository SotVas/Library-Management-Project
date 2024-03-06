function updateOrderAndStatus(orderId, action) {
    var url = (action === 'accept') ? '/orders/acceptOrder' : '/orders/denyOrder';
    var data = { orderId: orderId };

    $.post({
        url: url,
        data: data,
        success: function (response) {
            // Update the UI with the new status
            var statusElement = $('#status-' + orderId);
            statusElement.text('Order ' + action.charAt(0).toUpperCase() + action.slice(1));

            // Display a message based on the action
            var messageElement = $('#message-' + orderId);
            if (action === 'accept') {
                messageElement.text('Order Accepted');
            } else {
                messageElement.text('Order Denied');
            }

            // Hide the buttons
            $('#acceptBtn-' + orderId).hide();
            $('#denyBtn-' + orderId).hide();

            // Call the updateStatus function to update the status without refreshing the entire page
            updateStatus(orderId);
        },
        error: function (error) {
            console.error('Error updating order status', error);
        }
    });
}


function updateStatus(orderId) {
    $.ajax({
        type: 'GET',
        url: '/orders/getStatus/' + orderId,
        success: function (data) {
            $('#statusText').text(data);
        },
        error: function () {
            console.error('Failed to update status.');
        }
    });
}

function acceptOrder(orderId) {
    $.post('/orders/acceptOrder', { orderId: orderId }, function(data) {
        // Log the response for debugging
        console.log(data);
        // Update the status on the page
        $('#status-' + orderId).text('Accepted');
        // Hide the buttons after action
        hideButtons(orderId);
    });
}



function denyOrder(orderId) {
    $.post('/orders/denyOrder', { orderId: orderId }, function(data) {
        // Log the response for debugging
        console.log(data);
        // Update the status on the page
        $('#status-' + orderId).text('Denied');
        // Hide the buttons after action
        hideButtons(orderId);
    });
}

function hideButtons(orderId) {
    // Hide the accept and deny buttons after the action
    $('#acceptBtn-' + orderId).hide();
    $('#denyBtn-' + orderId).hide();
}

// JavaScript function for filtering orders based on the search input

function filterOrders() {
    var searchInput = document.getElementById('searchInput').value.trim().toLowerCase();

    var orders = document.querySelectorAll('.content-container .order-card');

    orders.forEach(function (order) {
        var usernameElement = order.querySelector('.username');
        var idElement = order.querySelector('.id') // Use class selector

        if (usernameElement && idElement) {
            var username = usernameElement.textContent.trim().toLowerCase();
            var id = idElement.textContent; // Extract text content

            // Check if the username or ID contains the search input
            var shouldDisplay = username.includes(searchInput) || id.includes(searchInput);

            // Toggle visibility based on the check
            order.style.display = shouldDisplay ? 'block' : 'none';
        }
    });
}


// Function to handle search on Enter key press
function handleEnterKey(event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // Prevent the default form submission behavior
        filterOrders();
    }
}

// Wait for the document to be fully loaded
document.addEventListener('DOMContentLoaded', function () {
    // Bind the filtering function to the click event on the search button
    document.getElementById('searchButton').addEventListener('click', filterOrders);

    // Bind the filtering function to the keypress event on the search input
    document.getElementById('searchInput').addEventListener('keypress', handleEnterKey);
});


window.onload = function () {
    updateOverdueOrdersCount();
}

function updateOverdueOrdersCount() {
    var overdueOrdersCount = document.querySelectorAll('.content-container .order-card .due-message').length;
    document.getElementById('overdueOrdersCount').innerText = overdueOrdersCount;
}

function showNotification(message) {
    // Set the notification message in the modal body
    document.getElementById('notificationModalBody').innerHTML = message;

    // Update overdue orders count in the navbar
    updateOverdueOrdersCount();

    // Show the modal
    $('#notificationModal').modal('show');
}
