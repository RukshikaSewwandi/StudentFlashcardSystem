// Wait for the DOM to be fully loaded
document.addEventListener("DOMContentLoaded", function () {
    // Redirect to addSubject.html when "Add Subject" button is clicked
    document.getElementById("add-subject").addEventListener("click", () => {
        window.location.href = "addSubject.html"; // Redirect to addSubject.html
    });

    // Search box toggle functionality
    let search = document.querySelector(".search-box");
    document.querySelector("#search-icon").onclick = () => {
        search.classList.toggle("active");
    };

    // Dropdown functionality (if needed)
    document.querySelector(".dropdown-btn").addEventListener("click", () => {
        document.querySelector(".dropdown-content").classList.toggle("active");
    });
});

// jQuery code
$(document).ready(function () {
    console.log("DOM fully loaded");

    // Fetch all subjects from the backend API
    function fetchSubjects() {
        $.ajax({
            url: "http://localhost:8080/api/studentflashcard/subject/GetAllSubjects",
            type: "GET",
            success: function (response) {
                if (response.code === "00") {
                    displaySubjects(response.content); // Display the list of subjects
                } else {
                    console.error("Failed to fetch subjects:", response.message);
                }
            },
            error: function (error) {
                console.error("Error fetching subjects:", error);
            }
        });
    }

    // Display the list of subjects
    function displaySubjects(subjects) {
        const subjectsList = $("#subjects-list");
        subjectsList.empty(); // Clear the existing content

        subjects.forEach((subject) => {
            const subjectHtml = `
                <div class="subject-item" data-subject-id="${subject.subjectID}">
                    <span class="subject-name">${subject.subjectName}</span>
                    <div class="button-container">
                        <button class="update-btn">Update</button>
                        <button class="delete-btn">Delete</button>
                    </div>
                </div>
            `;
            subjectsList.append(subjectHtml);
        });

        // Add event listener for subject name click
        $(".subject-name").on("click", function () {
            const subjectId = $(this).closest(".subject-item").data("subject-id");
            window.location.href = `subtopic.html?subjectId=${subjectId}`; // Redirect to subtopic page with subject ID
        });

        // Add event listeners for Update and Delete buttons
        $(".update-btn").on("click", function () {
            const subjectId = $(this).closest(".subject-item").data("subject-id");
            updateSubject(subjectId);
        });

        $(".delete-btn").on("click", function () {
            const subjectId = $(this).closest(".subject-item").data("subject-id");
            deleteSubject(subjectId);
        });
    }

    // Handle Update button click
    function updateSubject(subjectId) {
        const newName = prompt("Enter the new subject name:");
        if (newName) {
            const subjectDTO = {
                subjectID: subjectId,
                subjectName: newName
            };

            $.ajax({
                url: "http://localhost:8080/api/studentflashcard/subject/updateSubject",
                type: "PUT",
                contentType: "application/json",
                data: JSON.stringify(subjectDTO),
                success: function (response) {
                    if (response.code === "00") {
                        //alert("Subject updated successfully!");
                        fetchSubjects(); // Refresh the list of subjects
                    } else {
                        alert("Failed to update subject: " + response.message);
                    }
                },
                error: function (error) {
                    console.error("Error updating subject:", error);
                }
            });
        }
    }

    // Handle Delete button click
    function deleteSubject(subjectId) {
        if (confirm("Are you sure you want to delete this subject?")) {
            $.ajax({
                url: `http://localhost:8080/api/studentflashcard/subject/deleteSubject/${subjectId}`,
                type: "DELETE",
                success: function (response) {
                    if (response.code === "00") {
                        alert("Subject deleted successfully!");
                        fetchSubjects(); // Refresh the list of subjects
                    } else {
                        alert("Failed to delete subject: " + response.message);
                    }
                },
                error: function (error) {
                    console.error("Error deleting subject:", error);
                }
            });
        }
    }

    // Fetch subjects when the page loads
    fetchSubjects();
});