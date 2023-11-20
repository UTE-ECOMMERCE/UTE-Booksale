import { stickyHeader, sliderHandler } from "./utils.js";

// ****************************************************************
// for dropdown menu
const dropdownEls = document.querySelectorAll(".dropdown");

dropdownEls.forEach((el) => {
  el.addEventListener("mouseover", () => {
    el.classList.add("drop");
  });
});

dropdownEls.forEach((el) => {
  el.addEventListener("mouseout", () => {
    el.classList.remove("drop");
  });
});

// ****************************************************************
// HANDLE BTN NAV MOBILE
const btnMobile = document.querySelector(".btn-mobile-nav");

btnMobile.addEventListener("click", () => {
  const nav = document.querySelector(".navigation").parentElement;
  nav.classList.toggle("nav-open");
});

// ****************************************************************
// HANDLE btn for mobile search bar
const btnMobileSearch = document.querySelector(".btn-search-mobile");
const btnCloseSearch = document.querySelector(".btn-close-search");
const searchBar = document.querySelector(".search-bar");

btnMobileSearch.addEventListener("click", () => {
  searchBar.classList.add("open");
});

btnCloseSearch.addEventListener("click", () => {
  searchBar.classList.remove("open");
});

btnCloseSearch.addEventListener("click", () => {});

// ****************************************************************
// slider
sliderHandler();

// LOAD DATA FROM DATA FROM APIs

// LOAD DATA FROM DATA FROM APIs

const booksLinkEl = document.querySelectorAll(".bs-link");

booksLinkEl.forEach((link) => {
  link.addEventListener("click", (event) => {
    event.preventDefault();
    const url = "http://localhost:8080/booksale/api/book/subcategory/" + event.target.getAttribute("data-number");
    const container = link
      .closest(".bookshelf")
      .querySelector(".books-container");
    loadDataBooks(url, container);
  });
});

//remove book cards
const removeBookCards = (container) => {
  const bookCards = container.querySelectorAll(".book-card");

  bookCards.forEach((card) => {
    card.remove();
  });
};

function loadDataBooks(url, container) {
  fetch(url)
    .then((response) => response.json())
    .then((data) => {
      // Update the booksContainer with the retrieved data
      const insertHtml = data.map((book) => renderCard(book)).join("");
      removeBookCards(container);
      container.insertAdjacentHTML("beforeend", insertHtml);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}


function renderCard(book) {
  return `<div class="book-card">
              <a href="/booksale/book/${book.bookId}" class="book-link">
                <span class="discount-tag">-${book.discount}%</span>
                <div class="img-container">
                  <img
                    src=${book.mainImg}
                    alt="Book Cover"
                    class="card-img"
                  />
                </div>
                <div class="card-body">
                  <p class="card-title">${book.title}</p>
                  <div class="rating" class="card-rating">
                    <span class="rating-number">4.8</span
                    ><span><ion-icon name="star"></ion-icon></span
                    ><span class="rating-text">Đã bán 200</span>
                  </div>
                  <div class="card-prices">
                    <span class="card-discount">${book.finalPrice}đ</span>
                    <span class="card-price">${book.price}đ</span>
                  </div>
                </div>
              </a>
            </div>`;
}

// ----------------------------------------------------------------
// HANDLE TAB ON CATEGORY BOOK
const categoryLinkEl = document.querySelectorAll(".bs-link");

categoryLinkEl.forEach((link) => {
  link.addEventListener("click", (event) => {
    const listContainer = event.target.closest(".bookshelf-list");
    removeSelectedLinks(listContainer);
    event.target.parentElement.classList.add("selected");
  });
});

// remove current selected link
function removeSelectedLinks(listContainer) {
  console.log(listContainer);
  listContainer.querySelectorAll(".bs-link").forEach((link) => {
    link.parentElement.classList.remove("selected");
  });
}

// sticky navigation
const sectionSliderEl = document.querySelector(".section-slider");
stickyHeader(sectionSliderEl);
