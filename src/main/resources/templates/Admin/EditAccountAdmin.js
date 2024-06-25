function toggleEdit(inputId) {
    var inputField = document.getElementById(inputId);
    if (inputField.readOnly) {
        inputField.removeAttribute('readonly'); // Remove readonly attribute
        inputField.focus(); // Set focus to the input field
    } else {
        inputField.setAttribute('readonly', true); // Add readonly attribute
    }
}
function showPopup() {
    var overlay = document.getElementById("overlay");
    var popup = document.getElementById("success-popup");
    
    overlay.style.display = "block";
    popup.style.display = "block";
}

function hidePopup() {
    var overlay = document.getElementById("overlay");
    var popup = document.getElementById("success-popup");
    
    overlay.style.display = "none";
    popup.style.display = "none";
}
