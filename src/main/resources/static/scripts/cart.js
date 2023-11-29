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
  const confirmOrderModal = document.querySelector(".confirm-modal");
  const smallCloseModal = document.querySelector(".close");
  const btnConfirmOrder = document.querySelector(".btn-confirm");



    // Initialize total price
      let totalPrice = 0;
      let totalOriginPrice = 0;
      let discountPrice = 0;
      let shippingFee = parseFloat(shippingFeeElement.textContent);

      createOrderBtn.addEventListener("click", (e) => {
         e.preventDefault();
         confirmOrderModal.style.display = "block";
      });

       // Close the modal when the OK button is clicked
        smallCloseModal.addEventListener("click", () => {
          confirmOrderModal.style.display = "none";
        });

        btnConfirmOrder.addEventListener("click", (event) => {
          event.preventDefault();
          addressForm.submit();
        });


      window.addEventListener("click", (event) => {
          if (event.target === confirmOrderModal) {
            confirmOrderModal.style.display = "none";
          }
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


      updateStatus = (id, status) => {
            const data = {
              id: id,
              chosen: status,
            };

            fetch("http://localhost:8080/booksale/cart/choose-item", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify(data),
            }).then((res) => {
              console.log(res);
            });

        };

      const handleChange = (event) => {
          const checkbox = event.target;
          const bookId = checkbox.closest('.cart-book__form').querySelector('.book-id').value;
          const isChecked = checkbox.checked;

          updateStatus(bookId, isChecked);
          updateTotalPrice();
      };

      // Add change event listeners to checkboxes
      checkboxes.forEach((checkbox) => {

        checkbox.addEventListener("change", handleChange);
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
         const hiddenInput = element.parentElement.querySelector(".book-id");
         return hiddenInput.value;
     }

       btnDeleteItems.forEach((btn) => {
         btn.addEventListener("click", (e) => {
             e.preventDefault();
             const bookId = getBookId(e.target.parentElement);
             deleteData(bookId);

             // Reload the window after deleting
             window.location.reload();
         })
       })

     // Event delegation for handling button clicks
     document.addEventListener("click", (e) => {
         const btn = e.target;

         if (btn.classList.contains("btn-decrease")) {
             e.preventDefault();
             const input = btn.parentElement.querySelector(".quantity-box input");
             const availableQuantity = btn.parentElement.querySelector(".available-quantity");

             const currentValue = parseInt(input.value);
             const availableQuantityValue = parseInt(availableQuantity.value);

             const increaseButton = btn.parentElement.querySelector('.btn-increase');
             if (currentValue < availableQuantityValue) {
                 increaseButton.disabled = false;
             }

             if (currentValue > 1) {
                 input.value = currentValue - 1;
             }

             const id = getBookId(btn.parentElement);
             const quantity = input.value;
             updateData(id, quantity);

             updateTotalPrice();
         } else if (btn.classList.contains("btn-increase")) {
             e.preventDefault();
             const input = btn.parentElement.querySelector(".quantity-box input");
             const availableQuantity = btn.parentElement.querySelector(".available-quantity");

             const currentValue = parseInt(input.value);
             const availableQuantityValue = parseInt(availableQuantity.value);

             if (currentValue == availableQuantityValue - 1) {
                 btn.disabled = true;
             }

             input.value = currentValue + 1;

             const id = getBookId(btn.parentElement);
             const quantity = input.value;
             updateData(id, quantity);

             updateTotalPrice();
         }


     });

});


const clearFormFields = () => {
  document.getElementById('fullName').value = '';
  document.getElementById('email').value = '';
  document.getElementById('street').value = '';
  document.getElementById('description').value = '';

  // Clear selected options for the dropdowns
  const provinceSelect = document.getElementById('province');
  const districtSelect = document.getElementById('district');
  const wardSelect = document.getElementById('ward');

  provinceSelect.selectedIndex = 0; // Reset province to default option
  districtSelect.selectedIndex = 0; // Reset district to default option
  wardSelect.selectedIndex = 0; // Reset ward to default option
};

const fetchUserData = async () => {
  try {
    const phoneNumber = document.getElementById('phone').value;
    const response = await fetch(`http://localhost:8080/booksale/api/cart/address?phone=${phoneNumber}`);
    const responseData = await response.json();

    if (responseData.code === 200) {
      const userData = responseData.data.address.userInformation;

      // Populate the form fields with received user data
      document.getElementById('fullName').value = userData.fullName;
      document.getElementById('email').value = userData.email;
      document.getElementById('street').value = responseData.data.address.street;
      document.getElementById('description').value = responseData.data.address.description;

      // Set selected options for the dropdowns
      const provinceSelect = document.getElementById('province');
      const districtSelect = document.getElementById('district');
      const wardSelect = document.getElementById('ward');

      // Iterate through the options and set the selected attribute for the matching option in provinces
      for (let i = 0; i < provinceSelect.options.length; i++) {
        if (provinceSelect.options[i].textContent.trim() === responseData.data.address.province) {
          provinceSelect.options[i].selected = true;
          const code = provinceSelect.options[i].value;

          // Fetch districts based on the selected province
          const districtUrl = `https://provinces.open-api.vn/api/p/${code}?depth=2`;
          await populateOptions(districtUrl, "district", "Chọn quận (huyện)");
          break;
        }
      }

      // Set selected option for district
      for (let i = 0; i < districtSelect.options.length; i++) {
        if (districtSelect.options[i].textContent === responseData.data.address.district) {
          districtSelect.options[i].selected = true;
          const code = districtSelect.options[i].value;
          const apiUrl = `https://provinces.open-api.vn/api/d/${code}?depth=2`;
          await populateOptions(apiUrl, "ward", "Chọn phường (xã)");
          break;
        }
      }

      // Set selected option for ward
      for (let i = 0; i < wardSelect.options.length; i++) {
        if (wardSelect.options[i].textContent === responseData.data.address.ward) {
          wardSelect.options[i].selected = true;
          break;
        }
      }
    } else {
      clearFormFields();
    }
  } catch (error) {
    console.error('There was a problem fetching user data:', error);
  }
};
