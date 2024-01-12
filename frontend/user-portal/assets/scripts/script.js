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

// Load More Stories Button
const LOAD_ITEMS_COUNT = 3;
const LOADING_TIME_IN_MILLISECONDS = 2000;

function loadmoreStories(stories) {
  let loadmoreButton = document.querySelector(".load-more.btn");
  let currentItems = 3;

  if (loadmoreButton) {
    loadmoreButton.addEventListener("click", (event) => {
      event.target.classList.add("show-loader");

      for (let index = currentItems; index < currentItems + LOAD_ITEMS_COUNT; index++) {
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
}


// Start: Backend API call, fetch data and load HTML content for stories
// TODO: Move to another module or script file

// import { TIMEOUT_SEC } from "./config";
const TIMEOUT_SEC = 30;

const mainContainerAsideElement = document.querySelector(".main .container .aside");
const mainContainerAsideHtml = mainContainerAsideElement ? mainContainerAsideElement.innerHTML : null;
const mainContainerAsideElementDisplayStyle = mainContainerAsideElement ? mainContainerAsideElement.style.display : 'none';

const mainContainerStoriesElementDisplayStyle = document.querySelector(".main .container .stories").style.display;

function renderLoadingScreen() {
  document.querySelector(".load-stories").innerHTML = `
        <div class="loader"></div>
        <h3 class="message">Loading stories..</h3>`;

  document.querySelector(".main .container .stories").style.display = "none";
  if (mainContainerAsideElement)
    mainContainerAsideElement.style.display = "none";
}

function loadElements() {
  document.querySelector(".load-stories").style.display = "none";

  if (mainContainerAsideElement) {
    mainContainerAsideElement.style.display = mainContainerAsideElementDisplayStyle;
    mainContainerAsideElement.innerHTML = mainContainerAsideHtml;
  }
}

function renderError() {
  document.querySelector().innerHTML = `
        <h3 class="message">Something wrong! </h3>
        <h3>Plese try again later.</h3>`;
}

async function getData() {
  let dataResponse = null;
  try {
    //const username = new URLSearchParams(window.location.search).get('username');
    dataResponse = await getJSON("http://localhost:8080/api/story/all");
    // dataResponse = await getJSON("https://api-gw.boimama.in/story/all");
  } catch (error) {
    renderError();
    return;
  }

  // const storyItems = Array.from(dataResponse);
  const storyItems = dataResponse;

  // Load elements post successful API call
  loadElements();

  document.querySelector(".main .container .stories").style.display = mainContainerStoriesElementDisplayStyle;

  let storyCardElememnt = ``;

  storyItems.forEach(storyItem => {
    // console.log(storyItem);

    let storyCardBannerElement = document.querySelector(".story-card .story-card-banner .story-banner-img");
    storyCardBannerElement.src = storyItem.imagePath;
    storyCardBannerElement.alt = storyItem.title;

    let storyRatingSpan = ``;
    for (let index = 0; index < 5; index++) {
      if (storyItem.rating > index) {
        storyRatingSpan = storyRatingSpan + `<span class="fa fa-star checked"></span>`;
      } else {
        storyRatingSpan = storyRatingSpan + `<span class="fa fa-star"></span>`;
      }
    }
    document.querySelector(".story-content-metadata-top").innerHTML = `
          <button class="story-topic text-tiny">${storyItem.category}</button>
          <span>
             ${storyRatingSpan}
          </span>`;

    let storyPath = window.location.href.includes("/pages") ? "./story.html" : "./pages/story.html"
    document.querySelector(".story-card .story-content-wrapper .story-name").href = storyPath + "?story=" + storyItem.id; // TODO: Story URL should have story-title. Update backend to support this
    document.querySelector(".story-card .story-content-wrapper .story-name").innerText = storyItem.title;
    document.querySelector(".story-card .story-content-wrapper .story-text").innerText = storyItem.content;

    let authorImageElement = document.querySelector(".story-card .author-details .author-image");
    authorImageElement.src = storyItem.imagePath; // TODO: Correct
    authorImageElement.alt = storyItem.title; // TODO: Correct

    let authorNameElement = document.querySelector(".story-card .author-details .author-name");
    authorNameElement.href = storyItem.imagePath; // TODO: Correct
    authorNameElement.innerText = storyItem.authorNames[0]; // TODO: Correct

    document.querySelector(".story-card .story-publish-date").innerText = formatDate(storyItem.publishedDate);
    document.querySelector(".story-card .story-length").innerText = storyItem.lengthInMins + " mins";
    
    storyCardElememnt = storyCardElememnt + 
          `<div class="story-card">` + document.querySelector(".story-card").innerHTML  + `</div>`;
  });
  
  document.querySelector(".story-card-group").innerHTML = storyCardElememnt;

  const stories = [...document.querySelectorAll(".story-card")];
  loadmoreStories(stories);
}

const timeout = function (s) {
  return new Promise(function (_, reject) {
    setTimeout(function () {
      reject(new Error(`Request took too long! Timeout after ${s} second`));
    }, s * 1000);
  });
};

const getJSON = async function (url) {
  try {
    const response = await Promise.race([fetch(url), timeout(TIMEOUT_SEC)]);
    response.add
    if (!response.ok) {
      throw new Error("${response.statusText} (${response.status})");
    }
    const data = await response.json();
    return data;
  } catch (error) {
    throw error;
  }
};

function formatDate(inputDate) {
  const dateObject = new Date(inputDate);
  
  const months = [
    'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'
  ];

  const year = dateObject.getFullYear();
  const month = months[dateObject.getMonth()];
  const day = dateObject.getDate();

  return `${month} ${day}, ${year}`;
}
// End: Backend API call, fetch data and load HTML content for stories


// Changes for 'scroll to the top button'
let scrollTop = document.getElementById("top");

let html = document.querySelector("html");
let menu = document.querySelector("#menu");
let header = document.querySelector(".header");

const init = function () {
  renderLoadingScreen(); // Waiting while loading server data.

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

  getData(); // API call, fetch data and load HTML content for stories
};
init();
