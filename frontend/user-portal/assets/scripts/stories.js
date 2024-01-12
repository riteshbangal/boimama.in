// View on stories page: List/Grid
// localStorage.setItem("currentStoryItems", LOAD_ITEMS_COUNT); // Moved to script.js; Clean this up later! 
localStorage.setItem("story-view", "list"); // Default
const storyCardGroupClassList =
  document.querySelector(".story-card-group").classList;

// List View
function listView() {
  if (storyCardGroupClassList.toggle("grid-view")) {
    storyCardGroupClassList.toggle("grid-view");
  }
  if (!storyCardGroupClassList.toggle("list-view")) {
    storyCardGroupClassList.toggle("list-view");
  }
  localStorage.setItem("story-view", "list");
  setStoryView("grid");
}

// Grid View
function gridView() {
  if (!storyCardGroupClassList.toggle("grid-view")) {
    storyCardGroupClassList.toggle("grid-view");
  }
  if (storyCardGroupClassList.toggle("list-view")) {
    storyCardGroupClassList.toggle("list-view");
  }

  localStorage.setItem("story-view", "grid");
  setStoryView("block");
}

function setStoryView(displayStyle) {
  const stories = [...document.querySelectorAll(".story-card")];
  for (
    let index = 0;
    index < localStorage.getItem("currentStoryItems");
    index++
  ) {
    if (stories[index]) {
      stories[index].style.display = displayStyle;
    }
  }
}

/**
 * Optional: Add active class to the current button (highlight it)
 */
let container = document.getElementById("view-stories-btn");
let viewButtons = container.getElementsByClassName("view-btn");
for (let i = 0; i < viewButtons.length; i++) {
  viewButtons[i].addEventListener("click", function () {
    let current = document.getElementsByClassName("active");
    current[0].className = current[0].className.replace(" active", "");
    this.className += " active";
  });
}
