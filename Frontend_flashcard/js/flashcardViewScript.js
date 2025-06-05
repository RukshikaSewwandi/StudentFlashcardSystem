$(document).ready(function () {
    console.log("DOM fully loaded");

    // Get the flashcard ID from the query parameter
    const urlParams = new URLSearchParams(window.location.search);
    const flashcardId = urlParams.get("flashcardId");

    if (!flashcardId || isNaN(flashcardId)) {
        alert("Invalid Flashcard ID!");
        window.location.href = "dashboard.html"; // Redirect to dashboard if the ID is invalid
        return;
    }

    // Fetch the flashcard details from the backend
    $.ajax({
        url: `http://localhost:8080/api/studentflashcard/flashcard/content/${flashcardId}`, // Use template literals
        type: "GET",
        success: function (response) {
            console.log("API Response:", response); // Debugging: Log the response
            if (response.code === "00") {
                // Sanitize and render the HTML content
                const flashcardContent = DOMPurify.sanitize(response.content); // Sanitize HTML
                $("#flashcard-content").html(flashcardContent); // Render HTML string
            } else {
                alert("Failed to fetch flashcard: " + response.message);
            }
        },
        error: function (error) {
            console.error("Error fetching flashcard:", error); // Debugging: Log the error
            console.error("Error response text:", error.responseText); // Debugging: Log the response text
            alert("An error occurred while fetching the flashcard.");
        }
    });

    // Handle Back button click
    $("#back-btn").on("click", function () {
        window.history.back(); // Go back to the previous page
    });
});