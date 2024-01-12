// Load More Stories Button
const LOAD_ITEMS_COUNT = 3;
localStorage.setItem("currentStoryItems", LOAD_ITEMS_COUNT);

const LOADING_TIME_IN_MILLISECONDS = 2000;

export function loadmoreStories(stories) {
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
// import { TIMEOUT_SEC } from "./config";
const TIMEOUT_SEC = 30;

const mainContainerAsideElement = document.querySelector(".main .container .aside");
const mainContainerAsideHtml = mainContainerAsideElement ? mainContainerAsideElement.innerHTML : null;
const mainContainerAsideElementDisplayStyle = mainContainerAsideElement ? mainContainerAsideElement.style.display : 'none';

const mainContainerStoriesElementDisplayStyle = document.querySelector(".main .container .stories").style.display;

export function renderLoadingScreen() {
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

export async function buildStoriesHTML() {
  let dataResponse = null;
  try {
    //const username = new URLSearchParams(window.location.search).get('username');
    dataResponse = await fetchData("http://localhost:8080/api/story/all");
    // dataResponse = await fetchData("https://api-gw.boimama.in/story/all");
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

const fetchData = async function (url) {
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