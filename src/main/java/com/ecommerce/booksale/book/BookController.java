package com.ecommerce.booksale.book;


import com.ecommerce.booksale.book.category.Category;
import com.ecommerce.booksale.book.category.CategoryMapper;
import com.ecommerce.booksale.book.category.CategoryService;
import com.ecommerce.booksale.book.subcategory.SubCategory;
import com.ecommerce.booksale.book.subcategory.SubCategoryService;
import com.ecommerce.booksale.exception.BookNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
@SessionAttributes("cart")
@Slf4j
public class BookController {

    private static final String BOOK_NOT_FOUND = "Book not found for id = ";
    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer PAGE_SIZE = 12;
    private final BookService bookService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final HttpServletRequest request;

    @GetMapping("/book/category/{category}")
    public String getBookCategory(@PathVariable("category") String kebabCategoryName,
                                  @RequestParam(value = "sub", required = false) Integer subCategoryId, Model model) {
        // get URI
        String requestURI = request.getRequestURI();
        // retrieve data from database
        int categoryId = CategoryMapper.mapToCategoryId(kebabCategoryName);
        // get list book

        List<BookDTO> books;
        if (subCategoryId != null) {
            books = bookService.getBookByCategoryIdAndSubCategoryId(categoryId, subCategoryId, DEFAULT_PAGE, PAGE_SIZE);
        } else {
            books = bookService.getBookByCategoryIdWithPaging(categoryId, DEFAULT_PAGE, PAGE_SIZE);
        }
        // get subcategory by category
        Category cat = categoryService.getCategoryById(categoryId);
        List<SubCategory> subCategories = subCategoryService.getSubcategoriesByCategory(cat);

        // add data to model
        model.addAttribute("path", kebabCategoryName);
        model.addAttribute("requestURI", requestURI);
        model.addAttribute("category", cat);
        model.addAttribute("books", books);
        model.addAttribute("subcategories", subCategories);
        // return view
        return "book";
    }

    @GetMapping("/book/{id}")
    public String accessBuyPage(@PathVariable("id") Integer bookId, Model model) {


        BookDTO foundBook = this.bookService.findBook(bookId);
        List<BookDTO> relevantBooks = this.bookService.getRelevantBooks(bookId);

        if (foundBook == null) {
            throw new BookNotFoundException(BOOK_NOT_FOUND + bookId);
        }


        model.addAttribute("book", foundBook);
        model.addAttribute("relevantBooks", relevantBooks);
        return "book-sell-page";
    }


}
