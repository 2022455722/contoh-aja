// Function to show the popup
function showPopup() {
    document.getElementById('overlay').style.display = 'block';
    document.getElementById('popup').style.display = 'block';
}

// Function to hide the popup
function hidePopup() {
    document.getElementById('overlay').style.display = 'none';
    document.getElementById('popup').style.display = 'none';
    document.getElementById('success-popup').style.display = 'none';
}

// Function to show the success popup
function showSuccessPopup() {
    document.getElementById('popup').style.display = 'none';
    document.getElementById('success-popup').style.display = 'block';
}

// Add event listeners after DOMContentLoaded
document.addEventListener('DOMContentLoaded', function () {
    // Event listener for the "No" button in the popup
    document.querySelector('.popup-no-btn').addEventListener('click', hidePopup);

    // Event listener for the "Close" button in the success popup
    document.querySelector('.popup-close-btn').addEventListener('click', hidePopup);

    // Event listener for the "Back to Homepage" button
    document.querySelector('.close-btn').addEventListener('click', function () {
        window.location.href = 'index.html';
    });

    // Event listener for the "Delete Account" button
    document.querySelector('.delete-btn').addEventListener('click', showPopup);
});


