package com.ecommerce.booksale.book;

import com.ecommerce.booksale.book.category.Category;
import com.ecommerce.booksale.book.category.CategoryDTO;
import com.ecommerce.booksale.book.category.CategoryRepository;
import com.ecommerce.booksale.book.category.CategoryService;
import com.ecommerce.booksale.book.subcategory.SubCategory;
import com.ecommerce.booksale.book.subcategory.SubCategoryRepository;
import com.ecommerce.booksale.book.subcategory.SubCategoryService;
import com.ecommerce.booksale.exception.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ecommerce.booksale.exception.ErrorMessages.BOOK_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private static final Integer DEFAULT_SIZE = 6;
    private static final Integer DEFAULT_PAGE = 0;
    private static final String RELEVANT_BOOK_NOT_FOUND = "Relebant book not found";
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    // get All books
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    //
    public List<Book> findTopBooks() {

        Page<Book> pageBooks = bookRepository.findAll(PageRequest.of(0, 5));

        return pageBooks.getContent();
    }


    public List<BookDTO> getBookByCategoryIdAndSubCategoryId(Integer categoryId, Integer subCategoryId, int page, int size) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.format(ErrorMessages.CATEGORY_NOT_FOUND, categoryId)));
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new SubCategoryNotFoundException(String.format(ErrorMessages.SUBCATEGORY_NOT_FOUND, subCategoryId)));
        Page<Book> foundBooks = this.bookRepository
                .findBySubcategories(subCategory, PageRequest.of(page, size));

        if (foundBooks.getContent().isEmpty() || foundBooks == null) {
            throw new NotFoundBookByCategoryAndSubCategory(ErrorMessages.BOOK_NOT_FOUND_BY_CATEGORY_AND_SUBCATEGORY);
        }

        List<BookDTO> listBookData = foundBooks.getContent().stream().map(BookMapper::toDTO).collect(Collectors.toList());


        return listBookData;

    }

    public List<BookDTO> getBookByCategoryIdWithPaging(int id, int pageNumber, int quantity) {
        Pageable pageable = PageRequest.of(pageNumber, quantity);
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            throw new CategoryNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
        }

        Page<Book> books = bookRepository.findByCategories(category, pageable);
        if (!books.hasContent()) {
            throw new BookNotFoundByCategory(String.format(ErrorMessages.BOOK_NOT_FOUND_BY_CATEGORY, category.getName()));
        }

        List<BookDTO> bookData = books.stream().map(BookMapper::toDTO).collect(Collectors.toList());

        return bookData;
    }

    public List<BookDTO> getRelevantBooks(int id) {

        try {
            Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);

            Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));


            Page<Book> books;
            if (book.getCategories().isEmpty()) {
                books = bookRepository.findAll(pageable);
            } else {
                books = bookRepository.findByCategories(book.getCategories().get(0), pageable);
            }


            // throw exception
            if (books.isEmpty() || books == null) {
                throw new RelevantBookNotFoundException(RELEVANT_BOOK_NOT_FOUND);
            }

            List<BookDTO> relevantBooks = books.getContent().stream().map(BookMapper::toDTO).collect(Collectors.toList());

            return relevantBooks;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }


    }


    public List<BookDTO> getBookBySubCategoryId(int id, Pageable pageable) {

        SubCategory subcategory = subCategoryService.getSubcategoryById(id);

        Page<Book> books = bookRepository.findBySubcategories(subcategory, pageable);

        // throw exception
        if (books.isEmpty() || books == null) {
            throw new BookNotFoundException("Mục sản phẩm đang trống");
        }

        List<BookDTO> dataBooks = books.getContent().stream().map(BookMapper::toDTO).collect(Collectors.toList());

        return dataBooks;
    }


    // this function will add data to the model, include three list of books: fiction book, children book and self-help books
    // this data will use for home page of the website
    public void getHomeBookData(Model model) {

        Map<String, CategoryDTO> categoryMap = categoryService.getHomeCategories();
        model.addAttribute("categoriesMap", categoryMap);

        Pageable pageable = PageRequest.of(0, 7);

        Page<Book> books ;
        List<BookDTO> bookData;

        // create list books base on categories
        for (String category : categoryMap.keySet()) {
            books = bookRepository.findBySubcategories(categoryMap.get(category).getSubcategories().get(0)
                    , pageable);

            bookData = books.getContent().stream().map(BookMapper::toDTO).collect(Collectors.toList());

            model.addAttribute(category + "Books", bookData);
        }

    }

    public BookPaging getPagingBooks(Category category, int currentPage, int itemsPerPage) {

        Pageable pageable = PageRequest.of(currentPage, itemsPerPage);
        Page<Book> bookPaging = bookRepository.findByCategories(category, pageable);

        // retrive book data from the Page<Book> object
        List<Book> booksRaw = bookPaging.getContent();

        // convert List<Book> to List<BookDTO>
        List<BookDTO> bookDTOList = booksRaw.stream().map(BookMapper::toDTO).collect(Collectors.toList());

        BookPaging bookPagingData = new BookPaging();

        // set property
        bookPagingData.setBooks(bookDTOList);
        bookPagingData.setTotalPages(bookPaging.getTotalPages());
        bookPagingData.setCurrentPage(bookPaging.getNumber());
        bookPagingData.setNumberOfBooks(bookPaging.getNumberOfElements());

        return bookPagingData;
    }

    public BookDTO findBook(Integer bookId) throws BookNotFoundException {
        Book foundBook = this.bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book was not found"));
        BookDTO bookData = BookMapper.toDTO(foundBook);
        return bookData;
    }


}
