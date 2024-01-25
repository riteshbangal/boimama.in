
// utils.js
const utils = {

    containsAny: (text, keywords) => {
        return keywords.some(keyword => text.includes(keyword));
    },

    formatDate: (inputDate) => {
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
      },

      findTopOccurringElements: (array, topCount) => {
        let elementCount = {};
      
        // Count occurrences of each element
        array.forEach(element => {
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
      },

      timeout: (seconds) => {
        return new Promise(function (_, reject) {
          setTimeout(function () {
            reject(new Error(`Request took too long! Timeout after ${seconds} second`));
          }, seconds * 1000);
        });
      },
    
};

export default utils;
