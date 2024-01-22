// Load More Stories Button
const LOAD_ITEMS_COUNT = 9;
localStorage.setItem("currentStoryItems", LOAD_ITEMS_COUNT);

const LOADING_TIME_IN_MILLISECONDS = 2000;

export function loadmoreStories(stories) {
  let loadmoreButton = document.querySelector(".load-more.btn");
  let currentItems = LOAD_ITEMS_COUNT;

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
const API_BASE_URL = "http://localhost:8080/api";
// const API_BASE_URL = "https://api-gw.boimama.in";

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
      dataResponse = await fetchData(API_BASE_URL + "/story/search?searchText=" + searchCategory + "&categorySearch=true");
    } else if (searchTag) {  // tag is non-empty
      dataResponse = await fetchData(API_BASE_URL + "/story/search?searchText=" + searchTag + "&categorySearch=true");
    } else if (searchText) {  // searchText is non-empty
      dataResponse = await fetchData(API_BASE_URL + "/story/search?searchText=" + searchText);
    } else { // searchText is empty or null; Fetch all;
      dataResponse = await fetchData(API_BASE_URL + "/story/all");
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
  let storyTopics = [];

  storyItems.forEach((storyItem) => {
    // console.log(storyItem);

    let storyCardBannerElements = document.querySelectorAll(".story-card .story-card-banner .story-banner-img, " + 
                                                            ".story-card .story-card-banner-mobile .story-banner-img");
    storyCardBannerElements.forEach(storyCardBannerElement => {
      storyCardBannerElement.src = storyItem.imagePath;
      storyCardBannerElement.alt = storyItem.title;
    });
 
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

    storyTopics.push(storyItem.category);

    let storyPath = window.location.href.includes("/pages") ? "./story.html" : "./pages/story.html";
    document.querySelector(".story-card .story-content-wrapper .story-name")
      .href = storyPath + "?story=" + storyItem.id; // TODO: Story URL should have story-title. Update backend to support this
    document.querySelector(".story-card .story-content-wrapper .story-name").innerText = storyItem.title;
    document.querySelector(".story-card .story-content-wrapper .story-text").innerText = storyItem.content;

    let authorImageElement = document.querySelector(".story-card .author-details .author-image");
    authorImageElement.src = API_BASE_URL + "/author/" + storyItem.authorIds[0] + "/image";
    authorImageElement.alt = storyItem.authorNames[0];

    let authorNameElement = document.querySelector(".story-card .author-details .author-name");
    authorNameElement.href = window.location.href.includes("/pages") ? "./coming-soon.html#coming-soon" : "./pages/coming-soon.html#coming-soon";
    authorNameElement.innerText = storyItem.authorNames[0];

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


  // Prepare Story topics for the side panels
  const ionIcons = [ // TODO: select as per relevancy 
    'alarm', 'at', 'basketball', 'beer', 'bicycle', 'book', 'brush', 'cafe', 'camera', 'car',
    'cloud', 'desktop', 'flash', 'football', 'globe', 'hammer', 'heart', 'home', 'ice-cream', 'key'
  ]; // Array of possible ion-icon names

  let storyTopicsHtml = ``;
  findTopOccurringElements(storyTopics, 4).forEach(storyTopic => {
    storyTopicsHtml = storyTopicsHtml + `
        <a href="./pages/stories.html?category=${storyTopic}" class="topic-btn">
          <div class="icon-box">
            <ion-icon name="${ionIcons[Math.floor(Math.random() * ionIcons.length)]}"></ion-icon>
          </div>
          <p>${storyTopic}</p>
        </a>`;
  });

  if (document.querySelector(".aside .topics")) {
    document.querySelector(".aside .topics").innerHTML = `<h2 class="h2">Topics</h2> ${storyTopicsHtml}`;
  }

  // Prepare Story tags for the side panels
  let storyTagsHtml = ``;
  findTopOccurringElements(storyTopics).forEach(storyTopic => {
    storyTagsHtml = storyTagsHtml + `
        <a href="./pages/stories.html?tag=${storyTopic}"><button class="hashtag">#${storyTopic}</button></a>`;
  });
  if (document.querySelector(".aside .tags .wrapper")) {
    document.querySelector(".aside .tags .wrapper").innerHTML = storyTagsHtml;
  }
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
    dataResponse = await fetchData(API_BASE_URL + "/story/" + storyId);
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
  authorNameMobileElement.href = "./coming-soon.html#coming-soon";
  authorNameMobileElement.innerText = storyItem.authorNames[0];

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

  const storyContent = storyItem.content;
  const stringWithHtmlLineBreak = storyContent.replace(/\n/g, '<br>'); // Convert \n into <br> for HTML rendering
  document.querySelector(".story-panel .story-content").innerHTML = stringWithHtmlLineBreak;

  document.querySelector(".aside .story-metadata .story-publish-date")
    .innerText = formatDate(storyItem.publishedDate);
  document.querySelector(".aside .story-metadata .story-length")
    .innerText = storyItem.lengthInMins + " mins";

  let authorImageElement = document.querySelector(".aside .author-metadata .author-image");
  authorImageElement.src = API_BASE_URL + "/author/" + storyItem.authorIds[0] + "/image";
  authorImageElement.alt = storyItem.authorNames[0];

  let authorNameElement = document.querySelector(".aside .author-metadata .author-name");
  authorNameElement.href = "./coming-soon.html#coming-soon";
  authorNameElement.innerText = storyItem.authorNames[0];
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

function findTopOccurringElements(arr, topCount) {
  let elementCount = {};

  // Count occurrences of each element
  arr.forEach(element => {
    elementCount[element] = (elementCount[element] || 0) + 1;
  });

  // Convert the element count object to an array of [element, count] pairs
  let countArray = Object.entries(elementCount);

  // Sort the array based on the count in descending order
  countArray.sort((a, b) => b[1] - a[1]);

  // Extract the top elements
  let topElements = countArray.slice(0, topCount);

  // Return only the elements (discard the counts)
  return topElements.map(pair => pair[0]);
}
