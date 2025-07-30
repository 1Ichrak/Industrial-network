// Change background to gray if input has any content
document.addEventListener("DOMContentLoaded", function () {
  const inputs = [
        document.getElementById("identifier"),
        document.getElementById("password")
    ];

inputs.forEach(input => {
    input.addEventListener("input", function () {
      if (input.value.trim() !== "") {
        input.style.backgroundColor = "rgba(25, 25, 25, 1)";
        input.style.border = "2px solid rgba(218, 218, 218, 1)";
      } else {
        input.style.backgroundColor = "transparent";
        input.style.color = "white";
        input.style.border = "2px solid rgb(90, 90, 90)";
      }
    });
  });
});


document.addEventListener("DOMContentLoaded", () => {
  document.querySelector('.login-form').addEventListener('submit', async function(e) {
    e.preventDefault(); // Prevent page reload

    const id = document.getElementById('identifier').value;
    const password = document.getElementById('password').value;

    const response = await fetch('login.php', {
      method: 'POST',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      body: new URLSearchParams({identifier: id, password: password})
    });

    const result = await response.json();
    const loginResult = document.getElementById('login_result');

    if (result.success) {
      // Notify JavaFX of success via a custom event or Java callback
      if (window.javaConnector) {
        window.javaConnector.loginSuccess(); // call Java method
      } else {
        console.log("Java connector not available.");
      }
    } else {
      loginResult.textContent = result.message;
      loginResult.style.color = 'red';
    }
  });
});
