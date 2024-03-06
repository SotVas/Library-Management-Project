// login.js
$(document).ready(function () {
    // Display a message when the page is ready
    console.log('Login page is ready.');

    // Form validation
    $('#formLogin').submit(function (event) {
        // Prevent the form from submitting
        event.preventDefault();

        // Validate username and password fields
        var username = $('input[name="username"]').val();
        var password = $('input[name="password"]').val();

        if (!username || !password) {
            alert('Please enter both username and password.');
        } else {
            // If validation passes, submit the form
            $('#formLogin').unbind('submit').submit();
        }
    });

    // Show/hide password toggle
    $('#showPassword').click(function () {
        var passwordField = $('input[name="password"]');
        var fieldType = passwordField.attr('type');

        if (fieldType === 'password') {
            passwordField.attr('type', 'text');
            $('#showPassword').removeClass('fa-eye').addClass('fa-eye-slash');
        } else {
            passwordField.attr('type', 'password');
            $('#showPassword').removeClass('fa-eye-slash').addClass('fa-eye');
        }
    });

        function updateClock() {
        var now = new Date();
        var hours = now.getHours();
        var minutes = now.getMinutes();
        var seconds = now.getSeconds();

        // Add leading zero if needed
        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        seconds = seconds < 10 ? '0' + seconds : seconds;

        var timeString = hours + ':' + minutes + ':' + seconds;
        document.getElementById('clock').innerText = timeString;

        // Update every second
        setTimeout(updateClock, 1000);
    }

        // Initial call to start the clock
        updateClock();

});
$(document).ready(function () {
    var overlay = $('.overlay');
    var inputFields = $('input[name="username"], input[name="password"]');
    var loginBox = $('#loginbox');
    var typing = false;

    // Function to hide overlay
    function hideOverlay() {
        overlay.css('opacity', 0);
    }

    // Function to show overlay
    function showOverlay() {
        overlay.css('opacity', 1);
    }

    // Function to show input fields
    function showInputFields() {
        inputFields.css('opacity', 1);
    }
    function hideInputFields() {
        inputFields.css('opacity', 0);
    }

    // Function to check if input fields are focused
    function areInputFieldsFocused() {
        return inputFields.is(':focus');
    }

    // Function to set typing to true
    function startTyping() {
        typing = true;
    }

    // Function to set typing to false
    function stopTyping() {
        typing = false;
    }

    // Hide overlay when user interacts with input fields
    inputFields.focus(function () {
        hideOverlay();
        showInputFields();
    });

    // Show overlay when input fields lose focus
    inputFields.blur(function () {
        showOverlay();
        hideInputFields();
    });

    // Hide overlay when typing
    inputFields.keydown(function () {
        hideOverlay();
        startTyping();
    });

    // Show overlay when typing is finished
    inputFields.keyup(function () {
        if (!areInputFieldsFocused()) {
            showOverlay();
        }
        stopTyping();
    });

    // Hide overlay when the mouse enters the login box
    loginBox.mouseenter(function () {
        hideOverlay();
        showInputFields();
    });

    // Show overlay when the mouse leaves the login box and not typing
    loginBox.mouseleave(function () {
        if (!areInputFieldsFocused() && !typing) {
            showOverlay();
            hideInputFields();
        }
    });
});