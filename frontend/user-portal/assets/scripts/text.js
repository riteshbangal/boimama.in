// Header: Banner Taglines
document.querySelector(".banner .container .h1").innerHTML = `
    Immerse yourself in endless <b>stories</b>
`;

document.querySelector(".banner .container .h3").innerHTML = `
    Stories that inspire, connect and entertain. 
    Let us take you on a journey through stories...
`;

document.querySelector(".banner .container .h1 b").style.color = "#c28c69";

// Contact Message
let contactLabel = document.querySelector(".aside .contact .wrapper p");
if (contactLabel) {
  contactLabel.innerHTML = `
    We're just a message away. Join the conversation and let us talk about it.
  `;
}

// Footer: Taglines
document.querySelector("footer .footer-text").innerHTML = `
    Experience the power of storyreading, come get lost in our tales.
`;

// Cookie Consent
document.querySelector(".cookie-consent-container .content p").innerHTML = `
    This website use cookies to help you have a superior and more relevant browsing experience on the website. 
    We serve cookies on this site to analyze traffic, remember your preferences, and optimize your experience.
`;
