package com.ecommerce.booksale.book;


import com.ecommerce.booksale.book.category.Category;
import com.ecommerce.booksale.book.category.CategoryMapper;
import com.ecommerce.booksale.book.category.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@AllArgsConstructor
@RequestMapping("/api/book")
public class BookRestController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private static final int itemsPerPage = 12;

    @GetMapping("/subcategory/{id}")
    List<BookDTO> getBooksBySubCategory(@PathVariable int id){
        Pageable pageable = PageRequest.of(0, 7);
        return bookService.getBookBySubCategoryId(id, pageable);
    }

    @GetMapping("/category/{categoryName}")
    BookPaging getBooksByCategory(@PathVariable String categoryName, @RequestParam("page") Integer pageNumber){
        int convertedId = CategoryMapper
                                .mapToCategoryId(categoryName);
        Category category = categoryService
                                .getCategoryById(convertedId);
        // book paging
        return bookService.getPagingBooks(category, pageNumber, itemsPerPage);
    }

}
