"use strict";

import { loadmoreStories, renderLoadingScreen, buildStoryHTML } from './DataService.js'

// Changes for 'scroll to the top button'
let scrollTop = document.getElementById("top");

let html = document.querySelector("html");
let menu = document.querySelector("#menu");
let header = document.querySelector(".header");

const init = function () {
    renderLoadingScreen(); // Waiting while loading server data.
    buildStoryHTML(); // API call, fetch data and load HTML content for a single story
  
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
  