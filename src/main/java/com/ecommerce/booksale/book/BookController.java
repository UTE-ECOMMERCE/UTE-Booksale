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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@SessionAttributes("cart")
@Slf4j
@RequestMapping("/book")
public class BookController {

    private static final String BOOK_NOT_FOUND = "Book not found for id = ";
    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer PAGE_SIZE = 12;
    private final BookService bookService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final HttpServletRequest request;

    @GetMapping("/category/{category}")
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

    @GetMapping("/{id}")
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


    @GetMapping("/search")
    public String searchBook(@RequestParam(value = "keyword") String keyword,
                             @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                             @RequestParam(value = "size", defaultValue = "12", required = false) Integer size,
                             Model model) {


        String message = "KẾT QUẢ TÌM KIẾM";
        if (keyword.isEmpty() || keyword == null) {
            message = "Vui lòng nhập từ khóa tìm kiếm";
            model.addAttribute("message", message);
            return "search-page";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<BookDTO> books = bookService.searchBook(keyword, pageable);
        log.info(books.getContent().toString());
        if (books.getContent().isEmpty()) {
            message = "Không tìm thấy kết qủa cho: " + keyword;
            model.addAttribute("message", message);
            return "search-page";
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("message", message);
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("currentPage", books.getNumber());
        model.addAttribute("books", books.getContent());
        return "search-page";
    }


}
