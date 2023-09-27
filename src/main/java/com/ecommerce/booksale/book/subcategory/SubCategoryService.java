package com.ecommerce.booksale.book.subcategory;


import com.ecommerce.booksale.book.category.Category;
import com.ecommerce.booksale.exception.BookNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    public List<SubCategory> getSubcategoriesByCategory(Category category){
        return subCategoryRepository.findByCategory(category);
    }

    public SubCategory getSubcategoryById(int id) {
        return subCategoryRepository.findById(id)
                .orElseThrow(()->new BookNotFoundException("Không tìm thấy danh mục " + id));
    }
}
