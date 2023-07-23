"use strict";

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

// Load More Button
const LOAD_ITEMS_COUNT = 3;
const LOADING_TIME_IN_MILLISECONDS = 2000;

const stories = [...document.querySelectorAll(".story-card")];

let loadmoreButton = document.querySelector(".load-more.btn");
let currentItems = 3;

if (loadmoreButton) {
  loadmoreButton.addEventListener("click", (event) => {
    event.target.classList.add("show-loader");

    for (
      let index = currentItems;
      index < currentItems + LOAD_ITEMS_COUNT;
      index++
    ) {
      setTimeout(function () {
        event.target.classList.remove("show-loader");
        if (stories[index]) {
          stories[index].style.display = "grid";
          if ("grid" === localStorage.getItem("story-view")) {
            stories[index].style.display = "block";
          }
        }
      }, LOADING_TIME_IN_MILLISECONDS);
    }

    currentItems += LOAD_ITEMS_COUNT;

    // Hide load button after fully load.
    if (currentItems >= stories.length) {
      setTimeout(function () {
        console.log("going to disable load-more button");
        event.target.classList.add("loaded");
      }, LOADING_TIME_IN_MILLISECONDS);
    }

    localStorage.setItem("currentStoryItems", currentItems);
  });
}

// Custom changes
let scrollTop = document.getElementById("top");

let html = document.querySelector("html");
let menu = document.querySelector("#menu");
let header = document.querySelector(".header");

const init = function () {
  // renderLoadingScreen(); // Waiting while loading server data.

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

  // getData();
};
init();
