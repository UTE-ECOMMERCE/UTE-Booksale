package com.ecommerce.booksale.book.category;


import com.ecommerce.booksale.book.subcategory.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private int categoryId;
    private String categoryName;
    private String kebabCaseName;

    private List<SubCategory> subcategories;


}
