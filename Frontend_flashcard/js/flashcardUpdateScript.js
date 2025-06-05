// Function to apply formatting to the selected text
function formatDoc(cmd, value = null) {
  if (value) {
    document.execCommand(cmd, false, value);
  } else {
    document.execCommand(cmd);
  }
}

// Function to handle file operations (new file, download as TXT or PDF)
function fileHandle(value) {
  const content = document.getElementById("content");
  const filename = document.getElementById("filename");

  if (value === "new") {
    // Clear the content and reset the filename
    content.innerHTML = "";
    filename.value = "untitled";
  } else if (value === "txt") {
    // Save content as a .txt file
    const blob = new Blob([content.innerText], { type: "text/plain" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `${filename.value}.txt`;
    link.click();
    URL.revokeObjectURL(url); // Clean up the URL object
  } else if (value === "pdf") {
    // Save content as a .pdf file using html2pdf
    html2pdf()
      .from(content)
      .save(filename.value)
      .catch((error) => {
        console.error("Error generating PDF:", error);
        alert("An error occurred while generating the PDF.");
      });
  }
}

// Function to toggle code view
function toggleCodeView() {
  const content = document.getElementById("content");
  const showCodeButton = document.getElementById("show-code");
  const isActive = showCodeButton.getAttribute("data-active") === "true";

  if (isActive) {
    content.innerHTML = content.textContent; // Show formatted content
    content.setAttribute("contenteditable", true);
    showCodeButton.setAttribute("data-active", "false");
  } else {
    content.textContent = content.innerHTML; // Show raw HTML
    content.setAttribute("contenteditable", false);
    showCodeButton.setAttribute("data-active", "true");
  }
}

// Function to save or update flashcard
function saveFlashcard(flashcardName, flashcardContent, flashcardId = null) {
  if (!flashcardName || !flashcardContent) {
    alert("Please enter a flashcard name and content.");
    return;
  }

  const flashcardDTO = {
    flashcardName: flashcardName,
    flashcardContent: flashcardContent,
    flashcardId: flashcardId, // Include flashcardId for updates
  };

  const method = flashcardId ? "PUT" : "POST"; // Use PUT for updates, POST for new

  $.ajax({
    url: flashcardId ? `/flashcards/${flashcardId}` : "/flashcards", // Adjust URL for updates
    type: method,
    contentType: "application/json",
    data: JSON.stringify(flashcardDTO),
    success: function (response) {
      if (response.code === "00") {
        alert(
          flashcardId
            ? "Flashcard updated successfully!"
            : "Flashcard saved successfully!"
        );
        // Redirect or refresh as needed
      } else if (response.code === "06") {
        alert("Flashcard already exists!");
      } else {
        alert("Error saving flashcard: " + response.message);
      }
    },
    error: function (error) {
      console.error("Error saving flashcard:", error);
      alert("An error occurred while saving the flashcard.");
    },
  });
}

// Function to fetch flashcard data by ID
function fetchFlashcardById(flashcardId) {
  $.ajax({
    url: `http://localhost:8080/api/studentflashcard/flashcard/content/${flashcardId}`,
    type: "GET",
    dataType: "json",
    success: function (response) {
      if (response.code === "00") {
        // Populate the text area with the fetched data
        $("#flashcard-name").val(response.content.flashcardName);
        $("#content").html(response.content.flashcardContent);
        console.log(response.content.flashcardContent.html());
      } else {
        console.error("Failed to fetch flashcard:", response.message);
      }
    },
    error: function (error) {
      console.error("Error fetching flashcard:", error);
    },
  });
}

// Initialize the page
$(document).ready(function () {
  // Get the flashcard ID from the query parameter
  const urlParams = new URLSearchParams(window.location.search);
  const flashcardId = urlParams.get("flashcardId");

  if (flashcardId) {
    // Fetch and populate the flashcard data if an ID is provided
    fetchFlashcardById(flashcardId);
  }

  // Save Flashcard Button Click Event
  $("#save-flashcard").on("click", function () {
    const flashcardName = $("#flashcard-name").val();
    const flashcardContent = $("#content").html();

    if (flashcardName && flashcardName.trim()) {
      saveFlashcard(flashcardName.trim(), flashcardContent, flashcardId);
    } else {
      alert("Please enter a flashcard name.");
    }
  });

  // Event Listeners for Toolbar Buttons
  const showCodeButton = document.getElementById("show-code");
  if (showCodeButton) {
    showCodeButton.addEventListener("click", toggleCodeView);
  } else {
    console.error("Element with ID 'show-code' not found.");
  }
});
