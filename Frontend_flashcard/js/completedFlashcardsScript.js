$(document).ready(function () {
    console.log("DOM fully loaded");

    // Fetch completed flashcards
    function fetchCompletedFlashcards() {
        $.ajax({
            url: "http://localhost:8080/api/studentflashcard/flashcard/getCompletedFlashcards",
            type: "GET",
            success: function (response) {
                if (response.code === "00") {
                    displayCompletedFlashcards(response.content);
                } else {
                    console.error("Failed to fetch completed flashcards:", response.message);
                }
            },
            error: function (error) {
                console.error("Error fetching completed flashcards:", error);
            },
        });
    }

    // Display completed flashcards
    function displayCompletedFlashcards(flashcards) {
        const completedFlashcardsList = $("#completed-flashcards-list");
        completedFlashcardsList.empty(); // Clear existing content

        if (flashcards.length === 0) {
            completedFlashcardsList.append("<p>No completed flashcards found.</p>");
        } else {
            flashcards.forEach((flashcard) => {
                const flashcardItem = `
                    <div class="completed-flashcard-item" data-flashcard-id="${flashcard.flashcardID}">
                        <span>${flashcard.flashcardName}</span>
                        <div class="flashcard-actions">
                            <button class="view-flashcard-btn"><i class='bx bx-book-reader'></i></button>
                        </div>
                    </div>
                `;
                completedFlashcardsList.append(flashcardItem);
            });
        }
    }

    // Fetch completed flashcards when the page loads
    fetchCompletedFlashcards();
});