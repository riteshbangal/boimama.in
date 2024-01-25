import config from '../../configuration/config.js'; // Configurations
import constants  from './constants.js'; // Constants
import utils from './utils.js'; // Utility functions

// Decide environment's associated configurations.
const environment = utils.containsAny(window.location.host, constants.LOCAL_HOSTS) ? "development" : "production";
const currentConfig = config[environment];

document
  .querySelector(".contact-form")
  .addEventListener("submit", function (event) {
    // Remove the 'error' class when the user clicks on the input
    // TODO: document.getElementById('visitorEmail').classList.remove('error-input');

    event.preventDefault();

    let messageElement = document.querySelector(".contact-form .message");

    // Get form values
    let visitorName = document.getElementById("visitorName").value;
    let visitorEmail = document.getElementById("visitorEmail").value;
    let visitorPhone = document.getElementById("visitorPhone").value;
    let message = document.getElementById("message").value;

    // Create an object to hold the form data
    var formData = {
      name: visitorName,
      email: visitorEmail,
      phone: visitorPhone,
      message: message
    };

    // Make a POST request using the Fetch API
    fetch(currentConfig.apiUrl + '/user/contact', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    })
    .then(response => {
      if (response.status === 400) {
        throw new Error('Please validate your inputs!');
      }
      if (!response.ok) {
        throw new Error('Something went wrong! Please try after sometimes.');
      }
      return response.json();
    })
    .then(data => {
      const responseMessage = data["response"];
      // console.log('Response Message:', responseMessage);
      if ("success".localeCompare(responseMessage) !== 0) {
        throw new Error('Received unsuccessful response');
      }

      // Display success message in HTML page
      messageElement.innerHTML = `
          <div class="message" style="margin-bottom:2rem; color:green">
            Your message has been sent and will be answered as soon as possible.
          </div>`;
      // Optionally, you can reset the form after a successful submission
      // document.getElementById("contact-form").reset();
    })
    .catch(error => {
      console.error('Error:', error);
      // Display error message in HTML page
      messageElement.innerHTML = `<div class="message" style="margin-bottom:2rem; color:red">${error}</div>`;
    });
  });

// TODO: Input validation
function validateInputs() {
  
  // Check if the input is empty, then add the 'error' class and display an error message
  var emailInput = document.getElementById('visitorEmail');
  if (emailInput.value.trim() === '') {
    emailInput.placeholder = "Please enter your email id";
    emailInput.classList.add('error-input');
    // TODO: Fix this JS Validation.
  }

  // Email validation (basic regex)
  var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(visitorEmail)) {
      //alert('Please enter a valid email address');
      return;
  }
}