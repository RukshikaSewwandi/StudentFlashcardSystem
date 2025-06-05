document.getElementById('register-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        alert('Passwords do not match');
        return;
    }

    const studentData = {
        name: username,
        email: email,
        password: password
    };

    fetch("http://localhost:8080/api/flashcard/signup", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(studentData),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json(); // Parse JSON only if the response is OK
    })
    .then(data => {
        if (data.code === '00') {
            //alert('Registration successful!');
            window.location.href = 'login.html'; // Redirect to login page
        } else if (data.code === '06') {
            alert('Email already exists');
        } else {
            alert('Registration failed');
        }
    })
    .catch((error) => {
        console.error('Error:', error);
        //alert('An error occurred while registering. Please try again.');
    });
});