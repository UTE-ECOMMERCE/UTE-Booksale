document.addEventListener("DOMContentLoaded", () => {
  // DOM ojects for mofidy quantity of selected book
  const btnDecrease = document.querySelector(".btn-decrease");
  const btnIncrease = document.querySelector(".btn-increase");
  const quantityBox = document.querySelector(".quantity-box input");
  // DOM objects for add to cart and buy now
  const btnAddToCart = document.querySelector(".btn-add-to-cart");
  const btnBuyNow = document.querySelector(".btn-buy");
  // for toggle between show more and show less
  const showMore = document.querySelector(".more");
  const bookDescriptionBox = document.querySelector(".book-description");
  // Get the modal and buttons
  const modal = document.querySelector(".modal");
  const closeModal = document.querySelector(".close");
  const modalCloseButton = document.querySelector("#modalCloseButton");
  const bookId = document.querySelector(".hidden-book-id").value;
  const errorMessage = document.querySelector('.error-message');


   const availableQuantity = parseInt(document.querySelector('.available-quantity').value, 10);

    // Update button visibility and show error message based on quantity
       function updateButtonAndMessage() {
           const selectedQuantity = parseInt(quantityBox.value, 10);
           const quantityExceedsAvailable = selectedQuantity > availableQuantity;

           btnBuyNow.disabled = quantityExceedsAvailable;
           btnAddToCart.disabled = quantityExceedsAvailable;
           btnIncrease.disabled = quantityExceedsAvailable;
           errorMessage.style.display = quantityExceedsAvailable ? 'block' : 'none';
       }

       // Check on page load
       updateButtonAndMessage();

       // Check when quantity changes
       quantityBox.addEventListener('change', function() {
           updateButtonAndMessage();
       });

  // decrease quantity of selected book
  btnDecrease.addEventListener("click", (event) => {
    event.preventDefault();
    let currentQuantity = +quantityBox.value;
    if (currentQuantity > 1) {
      currentQuantity--;
    }
    quantityBox.value = "" + currentQuantity;
    btnDecrease.blur();
     updateButtonAndMessage();
  });

  // increase quantity of selected book
  btnIncrease.addEventListener("click", (event) => {
    event.preventDefault();
    let currentQuantity = +quantityBox.value;
    currentQuantity++;
    quantityBox.value = "" + currentQuantity;
    btnIncrease.blur();
    updateButtonAndMessage();

  });


  // show more or show less
  showMore.addEventListener("click", () => {
    bookDescriptionBox.classList.toggle("show-more");
    if (bookDescriptionBox.classList.contains("show-more")) {
      showMore.textContent = "Thu gọn";
    } else {
      showMore.textContent = "Xem thêm";
    }
  });

  btnAddToCart.addEventListener("click", (e) => {
      const requestData = {
         id: bookId,
         quantity: quantityBox.value,
      };
      console.log(requestData);

      e.preventDefault();
      fetch("http://localhost:8080/booksale/cart/add-to-cart", {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
          },
          body: JSON.stringify(requestData) // Send requestData directly without wrapping it in another object
      })
      .then((res) => {
          if (!res.ok) {
              throw new Error("Network response was not ok.");
          }
          console.log(res);
      })
      .then((data) => {
          modal.style.display = "block";
      })
      .catch((error) => {
          console.error("Error:", error);
      });
  });


  // Close the modal when the close button (×) is clicked
  closeModal.addEventListener("click", () => {
    modal.style.display = "none";
  });

  // Close the modal when the OK button is clicked
  modalCloseButton.addEventListener("click", () => {
    modal.style.display = "none";
  });

  // Close the modal if the user clicks outside of it
  window.addEventListener("click", (event) => {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  });
});
