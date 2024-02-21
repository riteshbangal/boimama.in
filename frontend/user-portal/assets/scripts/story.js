"use strict";

import { renderLoadingScreen, buildStoryHTML } from './DataService.js'

const init = function () {
    renderLoadingScreen(); // Waiting while loading server data.
    buildStoryHTML(); // API call, fetch data and load HTML content for a single story
  };
  
init();
  