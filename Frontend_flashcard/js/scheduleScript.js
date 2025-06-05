$(document).ready(function () {
    console.log("DOM fully loaded");

    // Get the flashcard ID from the query parameter
    const urlParams = new URLSearchParams(window.location.search);
    const flashcardId = urlParams.get("flashcardId");

    if (!flashcardId) {
        alert("Flashcard ID not found!");
        window.location.href = "dashboard.html"; // Redirect to dashboard if no flashcard ID is provided
        return;
    }

    // Function to fetch and display schedules
    function fetchSchedules() {
        $.ajax({
            url: `http://localhost:8080/api/studentflashcard/schedule/getScheduleByFlashcard/${flashcardId}`,
            type: "GET",
            success: function (response) {
                if (response.code === "00" && response.content) {
                    displaySchedules(response.content);
                } else {
                    console.error("Failed to fetch schedules:", response.message);
                }
            },
            error: function (error) {
                console.error("Error fetching schedules:", error);
            },
        });
    }

    // Function to display schedules in the table
    function displaySchedules(schedule) {
        const schedulesList = $("#schedules-list");
        schedulesList.empty(); // Clear existing content

        if (schedule) {
            const row = `
                <tr>
                    <td>${schedule.startingDate}</td>
                    <td>${schedule.numberOfDays}</td>
                    <td>${schedule.studyTime}</td>
                    <td><button class="delete-btn" data-schedule-id="${schedule.id}">Delete</button></td>
                </tr>
            `;
            schedulesList.append(row);
        } else {
            schedulesList.append("<tr><td colspan='4'>No schedules found</td></tr>");
        }

        // Attach event listener to delete buttons
        $(".delete-btn").on("click", function () {
            const scheduleId = $(this).data("schedule-id");
            deleteSchedule(scheduleId);
        });
    }

    // Function to delete a schedule
    function deleteSchedule(scheduleId) {
        if (confirm("Are you sure you want to delete this schedule?")) {
            $.ajax({
                url: `http://localhost:8080/api/studentflashcard/schedule/deleteSchedule/${scheduleId}`,
                type: "DELETE",
                success: function (response) {
                    if (response === "Schedule deleted successfully") {
                        alert("Schedule deleted successfully!");
                        fetchSchedules(); // Refresh the list of schedules
                    } else {
                        alert("Failed to delete schedule: " + response);
                    }
                },
                error: function (error) {
                    console.error("Error deleting schedule:", error);
                },
            });
        }
    }

    // Handle form submission
    $("#schedule-form").on("submit", function (event) {
        event.preventDefault();

        const startingDate = $("#starting-date").val();
        const numberOfDays = $("#number-of-days").val();
        const studyTime = $("#study-time").val();

        if (!startingDate || !numberOfDays || !studyTime) {
            alert("Please fill out all fields.");
            return;
        }

        const scheduleDTO = {
            flashcardID: parseInt(flashcardId),
            startingDate: startingDate,
            numberOfDays: parseInt(numberOfDays),
            studyTime: studyTime
        };

        // Send the request to save the schedule
        $.ajax({
            url: "http://localhost:8080/api/studentflashcard/schedule/saveSchedule",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(scheduleDTO),
            success: function (response) {
                if (response === "Schedule saved successfully") {
                    alert("Schedule created successfully!");
                    fetchSchedules(); // Refresh the list of schedules
                } else {
                    alert("Error creating schedule: " + response);
                }
            },
            error: function (error) {
                console.error("Error creating schedule:", error);
                alert("An error occurred while creating the schedule.");
            },
        });
    });

    // Fetch schedules when the page loads
    fetchSchedules();
});