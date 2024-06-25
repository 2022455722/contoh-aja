document.addEventListener('DOMContentLoaded', function () {
    // Form submission handler
    document.getElementById('registration-form').addEventListener('submit', function (event) {
        event.preventDefault(); // Prevent the form from submitting

        // Display the popup
        document.getElementById('popup').style.display = 'block';
        // Display the overlay
        document.getElementById('overlay').style.display = 'block';
    });

    // Close popup handler
    document.getElementById('close-popup').addEventListener('click', function () {
        // Close the popup
        document.getElementById('popup').style.display = 'none';
        // Hide the overlay
        document.getElementById('overlay').style.display = 'none';
    });

    // Login button handler
    document.getElementById('login').addEventListener('click', function () {
        // Redirect to the login page
        window.location.href = 'index.html';
    });
});
