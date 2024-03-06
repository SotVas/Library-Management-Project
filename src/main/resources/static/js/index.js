document.addEventListener("DOMContentLoaded", function () {
    // Get the password input element and the showPassword icon
    var passwordInput = document.getElementById("password");
    var showPasswordIcon = document.getElementById("showPassword");

    // Get the toggle button
    var toggleButton = document.getElementById("toggleButton");

    // Add click event listener to the showPassword icon
    if (showPasswordIcon) {
        showPasswordIcon.addEventListener("click", function () {
            // Toggle the password visibility
            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                showPasswordIcon.classList.remove("fa-eye");
                showPasswordIcon.classList.add("fa-eye-slash");
            } else {
                passwordInput.type = "password";
                showPasswordIcon.classList.remove("fa-eye-slash");
                showPasswordIcon.classList.add("fa-eye");
            }
        });
    }

    // Add click event listener to the toggle button
    if (toggleButton) {
        toggleButton.addEventListener("click", function () {
            // Add your toggle functionality here
            console.log("Toggle button clicked!");
        });
    }
});
