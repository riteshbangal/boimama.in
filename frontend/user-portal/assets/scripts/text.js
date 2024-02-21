// Header: Banner Taglines
document.querySelector(".banner .container .h1").innerHTML = `
    বাংলা গল্প পড়ুন, <b>বইমামার</b>  সাথে।
`;

// document.querySelector(".banner .container .h3").innerHTML = `
//     ছোটবেলায় স্কুল থেকে ফিরে বা ছুটির দিনে, বাংলা গল্পের বই নিয়ে সময় কাটায়নি এমন বাঙালি নেই বললেই চলে..
// `;
document.querySelector(".banner .container .h3").innerHTML = `
    হাসির গল্প, প্রেমের গল্প, রোমাঞ্চকর অ্যাডভেঞ্চারের গল্প, গায়ে কাঁটা দেওয়া ভূতের গল্প, রহস্য গল্প, 'বইমামা'-তে আছে নানারকমের স্বাদ। 
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
