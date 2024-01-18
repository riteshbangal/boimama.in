"use strict";

// Add a submit event listener to the form
document.getElementById('searchForm').addEventListener('submit', function (event) {
    // Prevent the default form submission behavior
    event.preventDefault();

    // Get the search input value
    var searchText = document.querySelector(".search-text").value;
    if (!searchText) { // TODO: Prevent XSS attack
        return; // Nothing happens if it's invalid input! 
    }

    /**
     * Perform the search logic here, 
     *  - redirect to the search results page (i.e. story.html page with searchText query parameter)
     */
    let storyPath = window.location.href.includes("/pages") ? "./stories.html" : "./pages/stories.html";
    window.location.href = storyPath + "?searchText=" + encodeURIComponent(searchText);
});
