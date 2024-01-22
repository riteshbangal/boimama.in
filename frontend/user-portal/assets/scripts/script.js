"use strict";

import { loadmoreStories, renderLoadingScreen, buildStoriesHTML } from './DataService.js'

// Set theme
if (localStorage.getItem("theme")) {
  document.body.className = localStorage.getItem("theme");
  // TODO: Fix theme button
}

// navbar variables
const nav = document.querySelector(".mobile-nav");
const navMenuBtn = document.querySelector(".nav-menu-btn");
const navCloseBtn = document.querySelector(".nav-close-btn");

// navToggle function
const navToggleFunc = function () {
  nav.classList.toggle("active");
};

navMenuBtn.addEventListener("click", navToggleFunc);
navCloseBtn.addEventListener("click", navToggleFunc);

// theme toggle variables
const themeBtn = document.querySelectorAll(".theme-btn");

for (let i = 0; i < themeBtn.length; i++) {
  themeBtn[i].addEventListener("click", function () {
    // toggle `light-theme` & `dark-theme` class from `body`
    // when clicked `theme-btn`
    document.body.classList.toggle("light-theme");
    document.body.classList.toggle("dark-theme");

    for (let i = 0; i < themeBtn.length; i++) {
      // When the `theme-btn` is clicked,
      // it toggles classes between `light` & `dark` for all `theme-btn`.
      themeBtn[i].classList.toggle("light");
      themeBtn[i].classList.toggle("dark");
    }

    localStorage.setItem("theme", document.body.className);
  });
}

// Cookie Consent
let cookieContainer = document.querySelector(".cookie-consent-container");
let cancelCookieButton = document.querySelector(".cancel");
let acceptCookieButton = document.querySelector(".accept");

cancelCookieButton.addEventListener("click", function () {
  cookieContainer.classList.remove("active");
});
acceptCookieButton.addEventListener("click", function () {
  cookieContainer.classList.remove("active");
  localStorage.setItem("cookieAccepted", "yes");
});

setTimeout(function () {
  let cookieAccepted = localStorage.getItem("cookieAccepted");
  if (cookieAccepted != "yes") {
    cookieContainer.classList.add("active");
  }
}, 2000);

// Changes for 'scroll to the top button'
let scrollTop = document.getElementById("top");

let html = document.querySelector("html");
let menu = document.querySelector("#menu");
let header = document.querySelector(".header");

const init = function () {

  // Get the current page pathname
  const currentPage = window.location.pathname;

  // Define a regular expression pattern for home page or index page
  const storiesPageIndexPattern = /(\/|\/index\.html|\/stories\.html)$/;
  
  // Check if the current page matches the pattern
  if (storiesPageIndexPattern.test(currentPage)) {
    renderLoadingScreen(); // Waiting while loading server data.
    buildStoriesHTML(); // API call, fetch data and load HTML content for stories
  }

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
