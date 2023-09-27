package com.ecommerce.booksale.book.category;


import org.springframework.util.StringUtils;

import java.text.Normalizer;

public class CategoryMapper {

    public static CategoryDTO toDTO (Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getName());
        categoryDTO.setKebabCaseName(convertToKebabCase(category.getName()));
        categoryDTO.setSubcategories(category.getSubCategories());

        return categoryDTO;
    }

    public static int mapToCategoryId(String kebabName){
        int theId = 1;
        switch (kebabName){
            case "tieu-thuyet":
                theId = 1;
                break;
            case "kinh-te":
                theId = 2;
                break;
            case "khoa-hoc":
                theId = 3;
                break;
            case "tam-ly":
                theId = 4;
                break;
            case "lich-su":
                theId = 5;
                break;
            case "sach-ky-nang":
                theId = 6;
                break;
            case "thieu-nhi":
                theId = 7;
                break;
            case "sach-moi":
                theId = 8;
                break;
            case "noi-bat":
                theId = 9;
                break;
            case "giam-gia":
                theId = 10;
                break;
        }

        return theId;
    }

    public static String convertToKebabCase(String name){

        String kebabCase = removeDiacriticalMarks(name.trim()).toLowerCase().replaceAll(" ", "-");

        return kebabCase;
    }

    public static String removeDiacriticalMarks(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
