

function validatePasswordMatch() {
    var newPassword = document.getElementById("newPassword").value;
    var confirmPassword = document.getElementById("confirmPassword").value;
    var passwordMatchError = document.getElementById("passwordMatchError");

    if (newPassword !== confirmPassword) {
        passwordMatchError.innerHTML = "Passwords do not match";
    } else {
        passwordMatchError.innerHTML = "";
    }
}



function validateForm() {
    var oldPassword = document.getElementById("oldPassword").value;
    var newPassword = document.getElementById("newPassword").value;
    var confirmPassword = document.getElementById("confirmPassword").value;



    if (oldPassword.trim() === "") {
        alert("Please enter the old password.");
        return false;
    }

    if (newPassword.trim() === "") {
        alert("Please enter the new password.");
        return false;
    }

    if (confirmPassword.trim() === "") {
        alert("Please confirm the new password.");
        return false;
    }

    if (newPassword !== confirmPassword) {
        alert("New password and confirm password do not match.");
        return false;
    }

    // Display the confirmation modal
    document.getElementById("confirmationModal").style.display = "block";

    // Prevent the default form submission
    return false;
}
function togglePasswordVisibility(inputId,eyeIconId) {
    var passwordInput = document.getElementById(inputId);
    var eyeIcon = document.getElementById(eyeIconId);
    var currentType = passwordInput.getAttribute("type");

    if (currentType === "password") {
        passwordInput.setAttribute("type", "text");
        eyeIcon.classList.remove("fa-eye");
        eyeIcon.classList.add("fa-eye-slash");
    } else {
        passwordInput.setAttribute("type", "password");
        eyeIcon.classList.remove("fa-eye-slash");
        eyeIcon.classList.add("fa-eye");
    }
}





function confirmSubmission() {
    console.log("confirmSubmission function called");
    // Display a confirmation dialog
    var isConfirmed = window.confirm("Are you sure you want to change the password?");
    if (validateForm()) {
        if (isConfirmed) {
            // Display success message
            alert("Password changed successfully.");
            return true; // If true, the form will be submitted
        } else {
            // Display canceled message
            alert("Password change canceled.");
            return false; // If false, the form won't be submitted
        }
    }
}







