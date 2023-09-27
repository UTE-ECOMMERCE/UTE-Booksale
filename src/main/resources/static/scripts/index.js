
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
const sliderHandler = function () {
  const slides = document.querySelectorAll(".slide");
  const btnPrev = document.querySelector(".btn-left");
  const btnNext = document.querySelector(".btn-right");
  const dotContainer = document.querySelector(".dots");

  let curSlide = 0;
  const maxSlide = slides.length;

  // Functions
  const createDots = function () {
    slides.forEach(function (_, i) {
      dotContainer.insertAdjacentHTML(
        "beforeend",
        `<div class="dot" data-slide="${i}"></div>`
      );
    });
  };

  const activateDot = function (slide) {
    document
      .querySelectorAll(".dot")
      .forEach((dot) => dot.classList.remove("dot--current"));

    document
      .querySelector(`.dot[data-slide="${slide}"]`)
      .classList.add("dot--current");
  };

  const goToSlide = function (slide) {
    slides.forEach(
      (s, i) => (s.style.transform = `translateX(${100 * (i - slide)}%)`)
    );
  };

  // Next slide
  const nextSlide = function () {
    if (curSlide === maxSlide - 1) {
      curSlide = 0;
    } else {
      curSlide++;
    }

    goToSlide(curSlide);
    activateDot(curSlide);
  };

  const prevSlide = function () {
    if (curSlide === 0) {
      curSlide = maxSlide - 1;
    } else {
      curSlide--;
    }
    goToSlide(curSlide);
    activateDot(curSlide);
  };

  const init = function () {
    goToSlide(0);
    createDots();

    activateDot(0);
  };
  init();

  // Event handlers
  btnNext.addEventListener("click", nextSlide);
  btnPrev.addEventListener("click", prevSlide);
};

sliderHandler();

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
  return `
  <div class="book-card">
  <span class="discount-tag">-${book.discount}%</span>
  <img
    src=${book.mainImg}
    alt="Book Cover"
    class="card-img"
  />
  <div class="card-body">
    <h3 class="card-title">${titleHandler(book.title)}</h3>
    <div class="rating card-rating">
      <span class="rating-number">4.8</span
      ><span><ion-icon name="star"></ion-icon></span
      ><span class="rating-text">Đã bán 200</span>
    </div>
    <div class="card-prices">
      <span class="card-discount">${book.finalPrice}đ</span>
      <span class="card-price">${book.price}đ</span>
    </div>
    <a href="/booksale/book/${book.bookId}" class="card-link">mua sách</a>
  </div>
</div>`;
}

function titleHandler(title) {
  let result = title;
  if (title.length > 50) {
    result = title.substring(0, 50) + "...";
  }
  return result;
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
  listContainer.querySelectorAll(".bs-link").forEach((link) => {
    link.parentElement.classList.remove("selected");
  });
}


//sticky navigation

const sectionSliderEl = document.querySelector(".section-slider");
const obs = new IntersectionObserver(
  (entries) => {
    const ent = entries[0];
    if (!ent.isIntersecting) {
      document.querySelector("header").classList.add("sticky");
    }
    if (ent.isIntersecting) {
      document.querySelector("header").classList.remove("sticky");
    }
  },
  {
    // in the viewport
    root: null,
    threshold: 0,
    rootMargin: "-180px",
  }
);
obs.observe(sectionSliderEl);
