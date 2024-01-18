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
          // console.log("going to disable load-more button");
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
const mainContainerAsideElementDisplayStyle = mainContainerAsideElement ? mainContainerAsideElement.style.display : "none";

const mainContainerStoriesElement = document.querySelector(".main .container .stories");
const mainContainerStoriesElementDisplayStyle = mainContainerStoriesElement ? mainContainerStoriesElement.style.display : "none";

const mainContainerStoryElement = document.querySelector(".main .container .story-panel");
const mainContainerStoryElementDisplayStyle = mainContainerStoryElement ? mainContainerStoryElement.style.display : "none";

export function renderLoadingScreen() {
  document.querySelector(".load-stories").innerHTML = `
        <div class="loader"></div>
        <h3 class="message">Loading ..</h3>`;

  if (mainContainerAsideElement)
    mainContainerAsideElement.style.display = "none";
  if (mainContainerStoriesElement)
    mainContainerStoriesElement.style.display = "none";
  if (mainContainerStoryElement)
    mainContainerStoryElement.style.display = "none";
}

function loadElements(_storyItems) {
  if (Array.isArray(_storyItems) && _storyItems.length > 0) { // This is for stories.html page
    document.querySelector(".load-stories").style.display = "none";
    mainContainerStoriesElement.style.display = mainContainerStoriesElementDisplayStyle;
  } else if (mainContainerStoriesElement) { // This is for stories.html page
    mainContainerStoriesElement.style.display = "none";
    document.querySelector(".load-stories").innerHTML = `
        <h3 class="message">No story found!</h3>
        <h4>Go for another search.</h4>`;
  } else if (mainContainerStoryElement) { // This is for story.html page
    document.querySelector(".load-stories").style.display = "none";
    mainContainerStoryElement.style.display = mainContainerStoryElementDisplayStyle;
  }

  if (mainContainerAsideElement) { // This is for index.html and story.html page
    mainContainerAsideElement.style.display = mainContainerAsideElementDisplayStyle;
    mainContainerAsideElement.innerHTML = mainContainerAsideHtml;
  }
}

function renderError() {
  document.querySelector(".load-stories").innerHTML = `
        <h3 class="message">Something wrong!</h3>
        <h4>Plese try again.</h4>`;
}

export async function buildStoriesHTML() {
  let dataResponse = null;
  try {
    const searchCategory = new URLSearchParams(window.location.search).get('category');
    const searchTag = new URLSearchParams(window.location.search).get('tag');
    const searchText = new URLSearchParams(window.location.search).get('searchText');

    if (searchCategory) { // category is non-empty
      dataResponse = await fetchData("http://localhost:8080/api/story/search?searchText=" + searchCategory + "&categorySearch=true");
      //dataResponse = await fetchData("https://api-gw.boimama.in/story/search?searchText=" + searchCategory + "&categorySearch=true");
    } else if (searchTag) {  // tag is non-empty
      dataResponse = await fetchData("http://localhost:8080/api/story/search?searchText=" + searchTag + "&categorySearch=true");
      //dataResponse = await fetchData("https://api-gw.boimama.in/story/search?searchText=" + searchTag + "&categorySearch=true");
    } else if (searchText) {  // searchText is non-empty
      dataResponse = await fetchData("http://localhost:8080/api/story/search?searchText=" + searchText);
      //dataResponse = await fetchData("https://api-gw.boimama.in/story/search?searchText=" + searchText);
    } else { // searchText is empty or null; Fetch all;
      dataResponse = await fetchData("http://localhost:8080/api/story/all");
      //dataResponse = await fetchData("https://api-gw.boimama.in/story/all");
    }
  } catch (error) {
    renderError();
    return;
  }

  // const storyItems = Array.from(dataResponse);
  const storyItems = dataResponse;

  // Load elements post successful API call
  loadElements(storyItems);

  let storyCardElememnt = ``;

  storyItems.forEach((storyItem) => {
    // console.log(storyItem);

    let storyCardBannerElement = document.querySelector(
      ".story-card .story-card-banner .story-banner-img"
    );
    storyCardBannerElement.src = storyItem.imagePath;
    storyCardBannerElement.alt = storyItem.title;

    let storyRatingSpan = ``;
    for (let index = 0; index < 5; index++) {
      if (storyItem.rating > index) {
        storyRatingSpan =
          storyRatingSpan + `<span class="fa fa-star checked"></span>`;
      } else {
        storyRatingSpan = storyRatingSpan + `<span class="fa fa-star"></span>`;
      }
    }
    document.querySelector(".story-content-metadata-top").innerHTML = `
        <a class="story-topic text-tiny">${storyItem.category}</a>
        <span>
            ${storyRatingSpan}
        </span>`;

    let storyPath = window.location.href.includes("/pages") ? "./story.html" : "./pages/story.html";
    document.querySelector(".story-card .story-content-wrapper .story-name")
      .href = storyPath + "?story=" + storyItem.id; // TODO: Story URL should have story-title. Update backend to support this
    document.querySelector(".story-card .story-content-wrapper .story-name").innerText = storyItem.title;
    document.querySelector(".story-card .story-content-wrapper .story-text").innerText = storyItem.content;

    let authorImageElement = document.querySelector(".story-card .author-details .author-image");
    authorImageElement.src = storyItem.imagePath; // TODO: Correct
    authorImageElement.alt = storyItem.title; // TODO: Correct

    let authorNameElement = document.querySelector(".story-card .author-details .author-name");
    authorNameElement.href = "#"; // TODO: Correct
    authorNameElement.innerText = storyItem.authorNames[0]; // TODO: Correct

    document.querySelector(".story-card .story-publish-date").innerText = formatDate(storyItem.publishedDate);
    document.querySelector(".story-card .story-length").innerText = storyItem.lengthInMins + " mins";

    storyCardElememnt = storyCardElememnt +
      `<div class="story-card">` +
      document.querySelector(".story-card").innerHTML +
      `</div>`;
  });

  document.querySelector(".story-card-group").innerHTML = storyCardElememnt;

  const stories = [...document.querySelectorAll(".story-card")];
  loadmoreStories(stories);
}
// End: Backend API call, fetch data and load HTML content for stories

// Start: Backend API call, fetch data and load HTML content for a single story
export async function buildStoryHTML() {
  let dataResponse = null;
  try {
    /**
     * Get the URLSearchParams object from the current URL;
     * Get the value of the 'story' parameter
     */
    const storyId = new URLSearchParams(window.location.search).get('story');
    console.log('Story ID:', storyId);
    dataResponse = await fetchData("http://localhost:8080/api/story/" + storyId);
    // dataResponse = await fetchData("https://api-gw.boimama.in/story/" + storyId);
  } catch (error) {
    renderError();
    return;
  }

  // const storyItems = Array.from(dataResponse);
  const storyItem = dataResponse;
  // console.log(storyItem);

  // Load elements post successful API call
  loadElements();

  document.querySelector(".story-panel .story-name").innerHTML = `<label>|</label> ${storyItem.title}`;

  let authorNameMobileElement = document.querySelector(".story-panel .mobile-story-metadata .author-name");
  authorNameMobileElement.href = storyItem.imagePath; // TODO: Correct
  authorNameMobileElement.innerText = storyItem.authorNames[0]; // TODO: Correct

  document.querySelector(".story-panel .mobile-story-metadata .story-publish-date")
    .innerText = formatDate(storyItem.publishedDate);
  document.querySelector(".story-panel .mobile-story-metadata .story-length")
    .innerText = storyItem.lengthInMins + " mins";

  let storyRatingSpan = ``;
  for (let index = 0; index < 5; index++) {
    if (storyItem.rating > index) {
      storyRatingSpan =
        storyRatingSpan + `<span class="fa fa-star checked"></span>`;
    } else {
      storyRatingSpan = storyRatingSpan + `<span class="fa fa-star"></span>`;
    }
  }

  const storyRatingSpanHtml = `
      <span>
          ${storyRatingSpan}
      </span>`;
  document.querySelector(".story-panel .mobile-story-metadata .story-rating").innerHTML = storyRatingSpanHtml;
  document.querySelector(".aside .story-metadata .story-rating").innerHTML = storyRatingSpanHtml;

  document.querySelector(".story-panel .story-content").innerHTML = storyItem.content;

  document.querySelector(".aside .story-metadata .story-publish-date")
    .innerText = formatDate(storyItem.publishedDate);
  document.querySelector(".aside .story-metadata .story-length")
    .innerText = storyItem.lengthInMins + " mins";

  let authorImageElement = document.querySelector(".aside .author-metadata .author-image");
  authorImageElement.src = storyItem.imagePath; // TODO: Correct
  authorImageElement.alt = storyItem.title; // TODO: Correct

  let authorNameElement = document.querySelector(".aside .author-metadata .author-name");
  authorNameElement.href = storyItem.imagePath; // TODO: Correct
  authorNameElement.innerText = storyItem.authorNames[0]; // TODO: Correct
}
// End: Backend API call, fetch data and load HTML content for a single story

// Common functions
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
    response.add;
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
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec",
  ];

  const year = dateObject.getFullYear();
  const month = months[dateObject.getMonth()];
  const day = dateObject.getDate();

  return `${month} ${day}, ${year}`;
}
