$(document).ready(function () {
    console.log("DOM fully loaded"); // Debugging: Check if DOM is loaded

    // Add event listener to the form
    $("#add-subject-form").on("submit", function (event) {
        event.preventDefault(); // Prevent the form from submitting the traditional way

        // Get the subject name from the input field
        const subjectName = $("#subject-name").val().trim();
        console.log("Subject Name:", subjectName); // Debugging: Check the input value

        // Create a SubjectDTO object
        const subjectDTO = {
            subjectID: "0",
            subjectName: subjectName
        };

        // Send the data to the backend API using jQuery AJAX
        $.ajax({
            url: "http://localhost:8080/api/studentflashcard/subject/saveSubject", // Correct URL
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(subjectDTO),
            success: function (data) {
                console.log("API Response:", data); // Debugging: Check the API response
                if (data.code === "00") {
                    //alert("Subject saved successfully!");
                    window.location.href = "/html/dashbord.html"; // Redirect to dashboard after saving
                } else if (data.code === "06") {
                    alert("Subject already exists!");
                } else {
                    alert("Error saving subject!");
                }
            },
            error: function (error) {
                console.error("Error:", error);
                alert("An error occurred while saving the subject.");
            }
        });
    });

    // Handle Cancel button click
    $("#cancel-btn").on("click", function () {
        window.location.href = "/html/dashbord.html"; // Redirect to dashboard
    });
});