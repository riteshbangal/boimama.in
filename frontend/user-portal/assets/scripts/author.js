"use strict";

import config from '../../configuration/config.js'; // Configurations
import constants from './constants.js'; // Constants
import utils from './utils.js'; // Utility functions

// Changes for 'scroll to the top button'
let scrollTop = document.getElementById("top");

let html = document.querySelector("html");
let menu = document.querySelector("#menu");
let header = document.querySelector(".header");


export async function buildAuthorHTML() {
  let dataResponse = null;
  try {
    const searchCategory = new URLSearchParams(window.location.search).get('category');
    const searchTag = new URLSearchParams(window.location.search).get('tag');
    const searchText = new URLSearchParams(window.location.search).get('searchText');

    const apiUrl = "https://api-gw-dev.boimama.in/author/all";
    // const apiUrl = "https://api-gw-dev.boimama.in/author/672d35f6-0642-47cb-98d0-09cc51ae5e2c/image";
    
    dataResponse = await fetchData(apiUrl);

    // console.log("My Author Response: ", dataResponse);
  } catch (error) {
    console.error(error);
    return;
  }
}


// Common asynchronous functions
const fetchData = async function (url) {
  try {
    // console.log("Promise.race URL: ", url);
    // const response = await Promise.race([fetch(url), utils.timeout(constants.TIMEOUT_SEC)]);
    const response = await Promise.race([
      fetch(url, {
        headers: {
          'Content-Type': 'application/json',
          // Add other headers as needed
        },
        
        // credentials: 'include', // Include credentials if required
      }),
      utils.timeout(constants.TIMEOUT_SEC)
    ]);


    response.add;
    if (!response.ok) {
      throw new Error("${response.statusText} (${response.status})");
    }
    const data = await response.json();
    return response;
  } catch (error) {
    throw error;
  }
};

function fetchAuthorData() {

  // const apiUrl = "https://api-gw-dev.boimama.in/author/all";
  const apiUrl = "https://api-gw-dev.boimama.in/author/672d35f6-0642-47cb-98d0-09cc51ae5e2c";

  fetch(apiUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      // Add any other headers as needed
    },
  })
  .then(response => {
    // Handle the response
    // console.log(response);
    return "success";
  })
  .then(data => {
    // Process the data
    // console.log(data);
  })
  .catch(error => {
    // Handle errors
    console.error('Error:', error);
  });

}

const init = function () {
  // buildAuthorHTML();    // With Promice.race
  fetchAuthorData(); // Direct etch

  menu.onclick = function toggleHeader() {
    menu.classList.toggle("fa-times");
    header.classList.toggle("toggle");
  };

  window.onscroll = function scrollWindow() {
    menu.classList.remove("fa-times");
    header.classList.remove("toggle");

    if (window.scrollY > 50) {
      scrollTop.style.display = "block";
    } else {
      scrollTop.style.display = "none";
    }
  };

  

};
init();


