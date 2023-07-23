document
  .querySelector(".contact-form")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    let name = document.getElementById("vistorName").value;
    let email = document.getElementById("vistorEmail").value;
    let phone = document.getElementById("vistorPhone").value;
    let message = document.getElementById("message").value;

    const data = { username: "example" };

    // TODO: Implemnt/Fix this
    Email.send({
      Host: "smtp.elasticemail.com",
      Username: "noreply_test@boimama.in",
      Password: "0C7CB05D671FFE181608132DB04E9AA4C472",
      To: "riteshbangal@gmail.com",
      From: "noreply_test@boimama.in",
      Subject: "This is the subject",
      Body: "And this is the body",
    }).then((message) => {
      // alert(message);
      let messageElement = document.querySelector(".contact-form .message");
      messageElement.innerHTML = `<div class="message" style="margin-bottom:3rem; color:red">${message}</div>`;
    });
    console.log("message sent");
  });
