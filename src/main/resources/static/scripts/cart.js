document.addEventListener("DOMContentLoaded", () => {
  const btnDecreases = document.querySelectorAll(".btn-decrease");
  const btnIncreases = document.querySelectorAll(".btn-increase");
  const btnDeleteItems = document.querySelectorAll(".btn-detele-item");
  const checkboxes = document.querySelectorAll(".checkbox");
    const totalPriceElement = document.querySelector(".total .money");
    const originPriceElement = document.querySelector(".subtotal .money");
    const dicountPriceElement = document.querySelector(".discount .money");
    const shippingFeeElement = document.querySelector(".shipping .money");
     const createOrderBtn = document.querySelector(".btn-order");
      const addressForm = document.querySelector(".address-form_cart");

      console.log(createOrderBtn);

    // Initialize total price
      let totalPrice = 0;
      let totalOriginPrice = 0;
      let discountPrice = 0;
      let shippingFee = parseFloat(shippingFeeElement.textContent);

      createOrderBtn.addEventListener("click", (e) => {
      e.preventDefault();
        console.log("Inside create order function");
          addressForm.submit();
        });

      // Function to update the total price based on the checkbox state
      const updateTotalPrice = () => {
        totalPrice = 0;
        totalOriginPrice = 0;
        discountPrice = 0;
        checkboxes.forEach((checkbox, index) => {
          if (checkbox.checked) {
            const quantityInput = checkbox
              .closest(".cart-book__form")
              .querySelector(".quantity-box input");
            const originPriceTag = checkbox
              .closest(".cart-book__form")
              .querySelector(".price-tag__origin");
            const salePriceElement = checkbox
              .closest(".cart-book__form")
              .querySelector(".price-tag__sale");
            const quantity = parseInt(quantityInput.value);
            const salePrice = parseFloat(salePriceElement.textContent);
            const originPriceValue = parseFloat(originPriceTag.textContent);
            const itemTotal = quantity * salePrice;
            const itemTotalOriginPrice = quantity * originPriceValue;
            totalPrice += itemTotal;
            totalOriginPrice += itemTotalOriginPrice;
          }
        });
        totalPriceElement.textContent = (totalPrice + shippingFee);
        originPriceElement.textContent = totalOriginPrice;
        dicountPriceElement.textContent = (totalPrice - totalOriginPrice);
      };

      // Add change event listeners to checkboxes
      checkboxes.forEach((checkbox) => {
        checkbox.addEventListener("change", updateTotalPrice);
      });


  updateData = (id, quantity) => {
    const data = {
      id: id,
      quantity: quantity,
    };
    fetch("http://localhost:8080/booksale/cart/update-quantity", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }).then((res) => {
      console.log(res);
    });
  };

  deleteData = (id) => {
    const data = {
          id: id,
          quantity: null,
        };
        fetch("http://localhost:8080/booksale/cart/delete", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(data),
        }).then((res) => {
          console.log(res);
        });
  }

  // Function to get the book ID
  function getBookId(element) {
  console.log(element);
    const hiddenInput = element.parentElement.querySelector(".book-id");
    return hiddenInput.value;
  }

  btnDeleteItems.forEach((btn) => {
    btn.addEventListener("click", (e) => {
        e.preventDefault();
        console.log("Inside delete function");
        const bookId = getBookId(e.target.parentElement);
        deleteData(bookId);

        // Reload the window after deleting
        window.location.reload();
    })
  })

  btnDecreases.forEach((btn) => {
    btn.addEventListener("click", (e) => {
      e.preventDefault();
      const input = e.target.parentElement.querySelector(".quantity-box input");
      const currentValue = parseInt(input.value);
      if (currentValue > 1) {
        input.value = currentValue - 1;
      }

      const id = getBookId(e.target.parentElement);
      const quantity = input.value;
      updateData(id, quantity);

      updateTotalPrice();
    });
  });

  btnIncreases.forEach((btn) => {
    btn.addEventListener("click", (e) => {
      e.preventDefault();
      const input = e.target.parentElement.querySelector(".quantity-box input");
      const currentValue = parseInt(input.value);
      input.value = currentValue + 1;

      const id = getBookId(e.target.parentElement);
      const quantity = input.value;
      updateData(id, quantity);

      updateTotalPrice();
    });
  });
});
