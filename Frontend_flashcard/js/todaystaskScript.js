$(document).ready(function () {
    console.log("DOM fully loaded");

    // Function to fetch and display flashcards for today
    function fetchFlashcardsForToday() {
        $.ajax({
            url: "http://localhost:8080/api/studentflashcard/schedule/getFlashcardsForToday",
            type: "GET",
            success: function (response) {
                if (response.code === "00" && response.content) {
                    displayFlashcards(response.content);
                } else {
                    console.error("Failed to fetch flashcards:", response.message);
                    $("#flashcards-list").html("<p>No flashcards to study today.</p>");
                }
            },
            error: function (error) {
                console.error("Error fetching flashcards:", error);
                $("#flashcards-list").html("<p>An error occurred while fetching flashcards.</p>");
            },
        });
    }

    // Function to display flashcards in boxes
    function displayFlashcards(flashcards) {
        const flashcardsList = $("#flashcards-list");
        flashcardsList.empty(); // Clear existing content

        if (flashcards.length > 0) {
            flashcards.forEach((flashcard) => {
                const flashcardBox = `
                    <div class="flashcard-box" data-flashcard-id="${flashcard.flashcardID}">
                        <span>${flashcard.flashcardName}</span>
                    </div>
                `;
                flashcardsList.append(flashcardBox);
            });
        } else {
            flashcardsList.append("<p>No flashcards to study today.</p>");
        }

        // Attach event listeners to flashcard boxes
        $(".flashcard-box").on("click", function () {
            const flashcardId = $(this).data("flashcard-id");
            viewFlashcard(flashcardId);
        });
    }

    // Function to handle flashcard box click (redirect to flashcard view page)
    function viewFlashcard(flashcardId) {
        if (!flashcardId) {
            alert("Flashcard ID not found!");
            return;
        }
        window.location.href = `flashcardView.html?flashcardId=${flashcardId}`;
    }

    // Fetch flashcards for today when the page loads
    fetchFlashcardsForToday();
});