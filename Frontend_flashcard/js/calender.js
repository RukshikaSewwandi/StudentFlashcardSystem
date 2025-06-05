// Global variables
let currentDate = new Date();
let events = {};

// DOM Elements
document.addEventListener('DOMContentLoaded', () => {
    // Initialize the calendar
    generateCalendar();

    // Add event listeners
    document.getElementById('prev-month').addEventListener('click', () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        generateCalendar();
    });

    document.getElementById('next-month').addEventListener('click', () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        generateCalendar();
    });

    // Modal close button
    document.querySelector('.close').addEventListener('click', () => {
        document.getElementById('event-modal').style.display = 'none';
    });

    // Close modal when clicking outside
    window.addEventListener('click', (e) => {
        if (e.target === document.getElementById('event-modal')) {
            document.getElementById('event-modal').style.display = 'none';
        }
    });

    // Handle form submission
    document.getElementById('add-event-form').addEventListener('submit', (e) => {
        e.preventDefault();
        addEvent();
    });

    // Load events from the server
    fetchEvents();
});

// Generate the calendar grid
function generateCalendar() {
    const calendarContainer = document.getElementById('calendar-container');
    const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
    const lastDay = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);

    // Update the month and year heading
    document.getElementById('month-year').textContent =
        `${firstDay.toLocaleString('default', { month: 'long' })} ${firstDay.getFullYear()}`;

    // Clear the calendar
    calendarContainer.innerHTML = '';

    // Add day headers
    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    daysOfWeek.forEach(day => {
        const dayHeader = document.createElement('div');
        dayHeader.className = 'calendar-header';
        dayHeader.textContent = day;
        calendarContainer.appendChild(dayHeader);
    });

    // Add blank spaces for days before the first day of the month
    const firstDayOfWeek = firstDay.getDay();
    for (let i = 0; i < firstDayOfWeek; i++) {
        const prevMonthDate = new Date(firstDay);
        prevMonthDate.setDate(prevMonthDate.getDate() - (firstDayOfWeek - i));
        addDayToCalendar(calendarContainer, prevMonthDate, true);
    }

    // Add days of the current month
    for (let i = 1; i <= lastDay.getDate(); i++) {
        const date = new Date(currentDate.getFullYear(), currentDate.getMonth(), i);
        addDayToCalendar(calendarContainer, date);
    }

    // Add blank spaces for days after the last day of the month
    const lastDayOfWeek = lastDay.getDay();
    for (let i = lastDayOfWeek + 1; i <= 6; i++) {
        const nextMonthDate = new Date(lastDay);
        nextMonthDate.setDate(nextMonthDate.getDate() + (i - lastDayOfWeek));
        addDayToCalendar(calendarContainer, nextMonthDate, true);
    }
}

// Add a day cell to the calendar
function addDayToCalendar(container, date, isOtherMonth = false) {
    const day = document.createElement('div');
    day.className = 'calendar-day';
    if (isOtherMonth) {
        day.classList.add('other-month');
    }

    // Check if it's today
    const today = new Date();
    if (date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth() &&
        date.getFullYear() === today.getFullYear()) {
        day.classList.add('today');
    }

    // Add date number
    const dateNumber = document.createElement('span');
    dateNumber.className = 'date-number';
    dateNumber.textContent = date.getDate();
    day.appendChild(dateNumber);

    // Format date as string for event lookup
    const dateString = formatDateString(date);

    // Add show icon if there are events
    if (events[dateString] && events[dateString].length > 0) {
        const showIcon = document.createElement('i');
        showIcon.className = 'bx bx-show view-icon';
        showIcon.addEventListener('click', (e) => {
            e.stopPropagation(); // Prevent the day click event from firing
            showEventPopup(dateString);
        });
        day.appendChild(showIcon);
    }

    // Add click event to open modal
    day.addEventListener('click', () => openEventModal(date));

    container.appendChild(day);
}

// Format date as YYYY-MM-DD for consistent storage
function formatDateString(date) {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

// Open the event modal for a specific date
function openEventModal(date) {
    const dateString = formatDateString(date);
    const modal = document.getElementById('event-modal');
    const selectedDateSpan = document.getElementById('selected-date');
    const eventDateInput = document.getElementById('event-date');
    const eventsList = document.getElementById('events-list');

    // Format the date for display
    selectedDateSpan.textContent = date.toLocaleDateString('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });

    // Set the hidden input value
    eventDateInput.value = dateString;

    // Clear the events list
    eventsList.innerHTML = '';

    // Add existing events for this day
    if (events[dateString] && events[dateString].length > 0) {
        events[dateString].forEach(event => {
            const eventItem = document.createElement('div');
            eventItem.className = 'event-item-full';

            const timeElement = document.createElement('span');
            timeElement.className = 'event-time';
            timeElement.textContent = event.eventTime || 'All day';

            const titleElement = document.createElement('span');
            titleElement.className = 'event-title';
            titleElement.textContent = event.title;

            const descElement = document.createElement('div');
            descElement.className = 'event-description';
            descElement.textContent = event.description || '';

            const deleteButton = document.createElement('span');
            deleteButton.className = 'delete-event';
            deleteButton.innerHTML = '&times;';
            deleteButton.addEventListener('click', () => deleteEvent(event.id));

            eventItem.appendChild(timeElement);
            eventItem.appendChild(titleElement);
            eventItem.appendChild(descElement);
            eventItem.appendChild(deleteButton);
            eventsList.appendChild(eventItem);
        });
    } else {
        const noEvents = document.createElement('p');
        noEvents.textContent = 'No events for this day';
        eventsList.appendChild(noEvents);
    }

    // Display the modal
    modal.style.display = 'block';
}

// Show a popup with event details
// Modify the showEventPopup function in calender.js

function showEventPopup(dateString) {
    const popup = document.createElement('div');
    popup.className = 'event-popup';

    // Create popup content
    const popupContent = document.createElement('div');
    popupContent.className = 'popup-content';

    // Add close button
    const closeButton = document.createElement('span');
    closeButton.className = 'close-popup';
    closeButton.innerHTML = '&times;';
    closeButton.addEventListener('click', () => {
        document.body.removeChild(popup);
    });
    popupContent.appendChild(closeButton);

    // Add event details in a table
    if (events[dateString] && events[dateString].length > 0) {
        const table = document.createElement('table');
        const thead = document.createElement('thead');
        const tbody = document.createElement('tbody');

        // Table header
        const headerRow = document.createElement('tr');
        const headers = ['Title', 'Time', 'Description', 'Action'];
        headers.forEach(headerText => {
            const th = document.createElement('th');
            th.textContent = headerText;
            headerRow.appendChild(th);
        });
        thead.appendChild(headerRow);
        table.appendChild(thead);

        // Table rows for events
        events[dateString].forEach(event => {
            const row = document.createElement('tr');

            const titleCell = document.createElement('td');
            titleCell.textContent = event.title;
            row.appendChild(titleCell);

            const timeCell = document.createElement('td');
            timeCell.textContent = event.eventTime || 'All day';
            row.appendChild(timeCell);

            const descCell = document.createElement('td');
            descCell.textContent = event.description || '';
            row.appendChild(descCell);

            const actionCell = document.createElement('td');
            const deleteIcon = document.createElement('i');
            deleteIcon.className = 'bx bxs-trash-alt delete-icon';
            deleteIcon.addEventListener('click', () => deleteEvent(event.id));
            actionCell.appendChild(deleteIcon);
            row.appendChild(actionCell);

            tbody.appendChild(row);
        });

        table.appendChild(tbody);
        popupContent.appendChild(table);
    } else {
        const noEvents = document.createElement('p');
        noEvents.textContent = 'No events for this day';
        popupContent.appendChild(noEvents);
    }

    popup.appendChild(popupContent);
    document.body.appendChild(popup);
}

// Add a new event
function addEvent() {
    const dateString = document.getElementById('event-date').value;
    const title = document.getElementById('event-title').value;
    const time = document.getElementById('event-time').value;
    const description = document.getElementById('event-description').value;

    // Create event object for Spring Boot backend
    const newEvent = {
        title: title,
        description: description,
        eventDate: dateString,
        eventTime: time || null
    };

    // Save to server
    fetch("http://localhost:8080/api/studentflashcard/event/saveEvent", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newEvent)
    })
    .then(response => response.json())
    .then(data => {
        if (data === "Event saved successfully") {
            // Initialize array if this is the first event for the day
            if (!events[dateString]) {
                events[dateString] = [];
            }

            // Add the new event to local storage
            events[dateString].push(newEvent);

            // Regenerate calendar to show new event
            generateCalendar();

            // Reset form and close modal
            document.getElementById('add-event-form').reset();
            openEventModal(new Date(dateString)); // Reopen the modal with updated events
        } else {
            alert('Failed to add event. Please try again.');
        }
    })
    .catch(error => {
        console.error('Error adding event:', error);
        //alert('Failed to add event. Please try again.');
    });
}

// Delete an event
function deleteEvent(eventId) {
    if (confirm('Are you sure you want to delete this event?')) {
        fetch(`http://localhost:8080/api/studentflashcard/event/deleteEvent/${eventId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data === "Event deleted successfully") {
                // Remove the event from the local events object
                for (const dateString in events) {
                    events[dateString] = events[dateString].filter(event => event.id !== eventId);
                }

                // Regenerate the calendar
                generateCalendar();

                // Close the popup
                const popup = document.querySelector('.event-popup');
                if (popup) {
                    document.body.removeChild(popup);
                }
            } else {
                alert('Failed to delete event. Please try again.');
            }
        })
        .catch(error => {
            console.error('Error deleting event:', error);
            alert('Failed to delete event. Please try again.');
        });
    }
}

// Fetch all events from the server
function fetchEvents() {
    const startDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
    const endDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);

    fetch(`http://localhost:8080/api/studentflashcard/event/getEventsByDateRange?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`)
    .then(response => response.json())
    .then(data => {
        if (data.code === VarList.RSP_SUCCESS) {
            events = data.content.reduce((acc, event) => {
                const dateString = event.eventDate;
                if (!acc[dateString]) {
                    acc[dateString] = [];
                }
                acc[dateString].push(event);
                return acc;
            }, {});
            generateCalendar(); // Regenerate with events
        } else {
            console.error('Error fetching events:', data.message);
        }
    })
    .catch(error => {
        console.error('Error fetching events:', error);
    });
}