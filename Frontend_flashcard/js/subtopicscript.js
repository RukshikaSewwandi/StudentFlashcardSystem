// Move deleteFlashcard to the global scope
function deleteFlashcard(flashcardId) {
  if (confirm("Are you sure you want to delete this flashcard?")) {
    $.ajax({
      url: `http://localhost:8080/api/studentflashcard/flashcard/deleteFlashcard/${flashcardId}`,
      type: "DELETE",
      success: function (response) {
        if (response.code === "00") {
          alert("Flashcard deleted successfully!");

          // Find the active subtopic and its dropdown
          const activeSubtopicItem = $(".subtopic-item.active");
          const subtopicId = activeSubtopicItem.data("subtopic-id");
          const flashcardDropdown = activeSubtopicItem.find(
            ".flashcard-dropdown"
          );

          // Refresh the list of flashcards for the active subtopic
          if (subtopicId && flashcardDropdown.length > 0) {
            fetchFlashcards(subtopicId, flashcardDropdown);
          } else {
            console.error("Active subtopic or flashcard dropdown not found.");
          }
        } else {
          alert("Failed to delete flashcard: " + response.message);
        }
      },
      error: function (error) {
        console.error("Error deleting flashcard:", error);
      },
    });
  }
}




// Function to mark a flashcard as completed
function markFlashcardAsCompleted(flashcardID, isCompleted) {
  fetch(`http://localhost:8080/api/studentflashcard/flashcard/markCompleted/${flashcardID}`, {
      method: 'PUT',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({ isCompleted: isCompleted }),
  })
  .then(response => response.json())
  .then(data => {
      if (data.code === '00') {
          alert('Flashcard marked as completed!');
          fetchCompletedFlashcards(); // Refresh the completed flashcards list
      } else {
          alert('Failed to mark flashcard as completed: ' + data.message);
      }
  })
  .catch(error => {
      console.error('Error:', error);
  });
}


// Fetch and render flashcards when the page loads
document.addEventListener('DOMContentLoaded', () => {
  fetchFlashcards(); // Fetch flashcards from the backend
});


function renderFlashcards(flashcards) {
  const flashcardsList = document.getElementById('flashcards-list');
  flashcardsList.innerHTML = ''; // Clear existing content

  flashcards.forEach(flashcard => {
      const flashcardBox = document.createElement('div');
      flashcardBox.className = 'flashcard-box';
      flashcardBox.setAttribute('data-flashcard-id', flashcard.flashcardID);

      // Flashcard Name
      const flashcardName = document.createElement('span');
      flashcardName.textContent = flashcard.flashcardName;

      // Checkbox to Mark as Completed
      const checkbox = document.createElement('input');
      checkbox.type = 'checkbox';
      checkbox.checked = flashcard.isCompleted; // Set checked if already completed
      checkbox.addEventListener('change', () => markFlashcardAsCompleted(flashcard.flashcardID, checkbox.checked));

      flashcardBox.appendChild(flashcardName);
      flashcardBox.appendChild(checkbox);
      flashcardsList.appendChild(flashcardBox);
  });
}

$(document).ready(function () {
  console.log("DOM fully loaded");

  // Get the subject ID from the query parameter
  const urlParams = new URLSearchParams(window.location.search);
  const subjectId = urlParams.get("subjectId");

  if (!subjectId) {
    alert("Subject ID not found!");
    window.location.href = "dashboard.html"; // Redirect to dashboard if no subject ID is provided
    return;
  }

  // Add event listener for the "Add Subtopic" button
  $("#add-subtopic").on("click", function () {
    $("#add-subtopic-dialog").css("display", "flex"); // Show the dialog box
  });

  // Add event listener for the "Cancel" button in the dialog
  $("#cancel-subtopic-btn").on("click", function () {
    $("#add-subtopic-dialog").css("display", "none"); // Hide the dialog box
  });

  // Add event listener for the "Create" button in the dialog
  $("#create-subtopic-btn").on("click", function () {
    const subtopicName = $("#subtopic-name").val().trim(); // Get the subtopic name
    if (subtopicName) {
      saveSubtopic(subtopicName, subjectId); // Save the subtopic
      $("#add-subtopic-dialog").css("display", "none"); // Hide the dialog box
    } else {
      alert("Please enter a subtopic name."); // Show an alert if the input is empty
    }
  });

  // Function to save a subtopic
  function saveSubtopic(subtopicName, subjectId) {
    // Validate input
    if (!subtopicName || !subjectId) {
      alert("Please provide a subtopic name and subject ID.");
      return;
    }

    // Create the subtopic DTO
    const subtopicDTO = {
      subtopicName: subtopicName,
      subjectID: subjectId,
    };

    // Send the request to save the subtopic
    $.ajax({
      url: "http://localhost:8080/api/studentflashcard/subtopic/saveSubtopic",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(subtopicDTO),
      success: function (response) {
        if (response.code === "00") {
          //alert("Subtopic saved successfully!");
          fetchSubjectAndSubtopics(subjectId); // Refresh the subtopic list
        } else if (response.code === "06") {
          alert("Subtopic already exists!");
        } else {
          alert("Error saving subtopic: " + response.message);
        }
      },
      error: function (error) {
        console.error("Error saving subtopic:", error);
        alert("An error occurred while saving the subtopic.");
      },
    });
  }

  // Fetch subject and subtopics when the page loads
  fetchSubjectAndSubtopics(subjectId);

  // Function to fetch subject and subtopics
  function fetchSubjectAndSubtopics(subjectId) {
    // Fetch subject name
    $.ajax({
      url: `http://localhost:8080/api/studentflashcard/subject/${subjectId}`,
      type: "GET",
      success: function (subjectResponse) {
        if (subjectResponse.code === "00") {
          // Display subject name in the heading
          $("#subject-name").text(subjectResponse.content.subjectName);
        } else {
          console.error("Failed to fetch subject:", subjectResponse.message);
        }
      },
      error: function (error) {
        console.error("Error fetching subject:", error);
      },
    });

    // Fetch subtopics
    $.ajax({
      url: `http://localhost:8080/api/studentflashcard/subtopic/getSubtopicsBySubject/${subjectId}`,
      type: "GET",
      success: function (subtopicResponse) {
        console.log("Subtopics API Response:", subtopicResponse); // Debugging: Log the response
        if (subtopicResponse.code === "00") {
          displaySubtopics(subtopicResponse.content); // Display the list of subtopics
        } else {
          console.error("Failed to fetch subtopics:", subtopicResponse.message);
        }
      },
      error: function (error) {
        console.error("Error fetching subtopics:", error);
      },
    });
  }

  // Function to display subtopics
  function displaySubtopics(subtopics) {
    console.log("Subtopics to display:", subtopics); // Debugging: Log the subtopics
    const subtopicsList = $("#subtopics-list");
    subtopicsList.empty(); // Clear the existing content

    if (subtopics.length === 0) {
      subtopicsList.append("<p>No subtopics found for this subject.</p>");
      return;
    }

    subtopics.forEach((subtopic) => {
      const subtopicHtml = `
                <div class="subtopic-item" data-subtopic-id="${subtopic.subtopicID}">
                    <span>${subtopic.subtopicName}</span>
                    <div class="button-container">
                        <i class='bx bxs-chevron-down dropdown-arrow'></i> 
                        <!-- Dropdown arrow -->
                        
                        <button class="add-flashcard-btn">Create Flashcard</button>
                        <button class="update-subtopic-btn">Rename</button>
                        <button class="delete-subtopic-btn">Delete</button>
                    </div>
                    <!-- Flashcard Dropdown -->
                    <div class="flashcard-dropdown" style="display: none;">
                        <div class="flashcard-list">
                            <!-- Flashcards will be dynamically added here -->
                        </div>
                    </div>
                </div>
            `;
      subtopicsList.append(subtopicHtml);
    });

    // Attach event listeners to dropdown arrows
    document.querySelectorAll(".dropdown-arrow").forEach((arrow) => {
      arrow.addEventListener("click", function () {
        const subtopicItem = arrow.closest(".subtopic-item");
        const flashcardDropdown = subtopicItem.querySelector(
          ".flashcard-dropdown"
        );
        const subtopicId = subtopicItem.dataset.subtopicId;

        // Toggle dropdown visibility
        flashcardDropdown.style.display =
          flashcardDropdown.style.display === "none" ? "block" : "none";

        // Fetch flashcards if dropdown is visible
        if (flashcardDropdown.style.display === "block") {
          fetchFlashcards(subtopicId, flashcardDropdown);
        }
      });
    });

    // Function to fetch and display flashcards
    function fetchFlashcards(subtopicId, flashcardDropdown) {
      $.ajax({
        url: `http://localhost:8080/api/studentflashcard/flashcard/getFlashcardsBySubtopic/${subtopicId}`,
        type: "GET",
        success: function (response) {
          if (response.code === "00") {
            displayFlashcards(response.content, flashcardDropdown);
          } else {
            console.error("Failed to fetch flashcards:", response.message);
          }
        },
        error: function (error) {
          console.error("Error fetching flashcards:", error);
        },
      });
    }

    // Function to display flashcards in the dropdown
    function displayFlashcards(flashcards, flashcardDropdown) {
      const flashcardList = flashcardDropdown.querySelector(".flashcard-list");
      flashcardList.innerHTML = ""; // Clear existing content

      if (flashcards.length === 0) {
        flashcardList.innerHTML =
          "<div class='no-flashcards'>No flashcards found</div>";
      } else {
        flashcards.forEach((flashcard) => {
          const flashcardItem = document.createElement("div");
          flashcardItem.className = "flashcard-item";
          flashcardItem.setAttribute(
            "data-flashcard-id",
            flashcard.flashcardID
          ); // Set the flashcard ID
          flashcardItem.innerHTML = `
                        <span>${flashcard.flashcardName}</span>
                        <div class="flashcard-actions">
                            <button class="view-flashcard-btn"><i class='bx bx-book-reader'></i></button>
                            <button class="edit-flashcard-btn"><i class='bx bxs-edit-alt'></i></button>
                            <button class="delete-flashcard-btn"><i class='bx bx-trash-alt'></i></button>
                            <button class="schedule-flashcard-btn">Shedule</i></button>
                            <input type="checkbox" class="mark-completed-checkbox" data-flashcard-id="${
                              flashcard.flashcardID
                            }" ${flashcard.isCompleted ? "checked" : ""}>
                        </div>
                    `;
          flashcardList.appendChild(flashcardItem);
        });

        // Attach event listeners to the flashcard buttons
        attachFlashcardEventListeners();

        // Add event listener for the checkbox
        document
          .querySelectorAll(".mark-completed-checkbox")
          .forEach((checkbox) => {
            checkbox.addEventListener("change", function () {
              const flashcardId = this.getAttribute("data-flashcard-id");
              const isCompleted = this.checked;
              markFlashcardAsCompleted(flashcardId, isCompleted);
            });
          });

        // Add event listener for the Schedule button
        $(".schedule-flashcard-btn").on("click", function () {
          const flashcardId = $(this)
            .closest(".flashcard-item")
            .data("flashcard-id");
          console.log("Schedule button clicked. Flashcard ID:", flashcardId); // Debugging log
          window.location.href = `schedule.html?flashcardId=${flashcardId}`; // Redirect to schedule.html
        });
      }
    }

    // Add event listeners for Rename and Delete buttons
    $(".update-subtopic-btn").on("click", function () {
      const subtopicId = $(this).closest(".subtopic-item").data("subtopic-id");
      updateSubtopic(subtopicId);
    });

    $(".delete-subtopic-btn").on("click", function () {
      const subtopicId = $(this).closest(".subtopic-item").data("subtopic-id");
      deleteSubtopic(subtopicId);
    });

    // Add event listener for Create Flashcard button
    $(".add-flashcard-btn").on("click", function () {
      const subtopicId = $(this).closest(".subtopic-item").data("subtopic-id");
      window.location.href = `flashcard.html?subtopicId=${subtopicId}`; // Redirect to flashcard.html
    });
  }

  // Handle Rename Subtopic button click
  function updateSubtopic(subtopicId) {
    const newName = prompt("Enter the new subtopic name:");
    if (newName) {
      const subtopicDTO = {
        subtopicID: subtopicId,
        subtopicName: newName,
        subjectID: subjectId, // Link the subtopic to the current subject
      };

      $.ajax({
        url: "http://localhost:8080/api/studentflashcard/subtopic/updateSubTopic",
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(subtopicDTO),
        success: function (response) {
          if (response.code === "00") {
            //alert("Subtopic renamed successfully!");
            fetchSubjectAndSubtopics(subjectId); // Refresh the list of subtopics
          } else {
            alert("Failed to rename subtopic: " + response.message);
          }
        },
        error: function (error) {
          console.error("Error renaming subtopic:", error);
        },
      });
    }
  }

  // Handle Delete Subtopic button click
  function deleteSubtopic(subtopicId) {
    if (confirm("Are you sure you want to delete this subtopic?")) {
      $.ajax({
        url: `http://localhost:8080/api/studentflashcard/subtopic/deleteSubtopic/${subtopicId}`,
        type: "DELETE",
        success: function (response) {
          if (response.code === "00") {
            //alert("Subtopic deleted successfully!");
            fetchSubjectAndSubtopics(subjectId); // Refresh the list of subtopics
          } else {
            alert("Failed to delete subtopic: " + response.message);
          }
        },
        error: function (error) {
          console.error("Error deleting subtopic:", error);
        },
      });
    }
  }
});

// Function to attach event listeners to flashcard buttons
function attachFlashcardEventListeners() {
  // View button
  $(".view-flashcard-btn").on("click", function () {
    const flashcardId = $(this).closest(".flashcard-item").data("flashcard-id");
    console.log("View button clicked. Flashcard ID:", flashcardId); // Debugging log
    viewFlashcard(flashcardId);
  });

  // Edit button
  $(".edit-flashcard-btn").on("click", function () {
    const flashcardId = $(this).closest(".flashcard-item").data("flashcard-id");
    console.log("Edit button clicked. Flashcard ID:", flashcardId); // Debugging log
    editFlashcard(flashcardId);
  });

  // Delete button
  $(".delete-flashcard-btn").on("click", function () {
    const flashcardId = $(this).closest(".flashcard-item").data("flashcard-id");
    console.log("Delete button clicked. Flashcard ID:", flashcardId); // Debugging log
    deleteFlashcard(flashcardId); // Call the globally defined deleteFlashcard function
  });
}

// Function to handle the "View" button click
function viewFlashcard(flashcardId) {
  if (!flashcardId) {
    alert("Flashcard ID not found!");
    return;
  }
  window.location.href = `flashcardView.html?flashcardId=${flashcardId}`; // Redirect to flashcardView.html
}

// Function to handle the "Edit" button click
function editFlashcard(flashcardId) {
  if (!flashcardId) {
    alert("Flashcard ID not found!");
    return;
  }
  window.location.href = `flashcardUpdate.html?flashcardId=${flashcardId}`; // Redirect to flashcardUpdate.html
}
