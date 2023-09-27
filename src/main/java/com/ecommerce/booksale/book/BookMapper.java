package com.ecommerce.booksale.book;
import com.ecommerce.booksale.book.category.Category;
import com.ecommerce.booksale.book.image.Image;
import com.ecommerce.booksale.book.subcategory.SubCategory;

import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookDTO toDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(book.getBookId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setPublicationYear(book.getPublicationYear());
        bookDTO.setDiscount(book.getDiscount());
        bookDTO.setMainImg(book.getMainImg());
        bookDTO.setDesc(book.getBookDescription());
        bookDTO.setPages(book.getTotalPages() == null ? 300 : book.getTotalPages());
        bookDTO.setActive(book.isActive());
        bookDTO.setFinalPrice(handlePrice(book.getPrice(), book.getDiscount()));
        bookDTO.setPrice(book.getPrice());
        bookDTO.setCoverType(book.getCoverType());
        bookDTO.setQuantity(book.getQuantity());
        bookDTO.setAuthorName(book.getAuthor().getName());
        bookDTO.setPublisherId(book.getPublisher().getPublisherId());

        List<Integer> categoryIds = book.getCategories().stream()
                .map(Category::getCategoryId)
                .collect(Collectors.toList());
        bookDTO.setCategoryIds(categoryIds);

        List<Integer> subcategoryIds = book.getSubcategories().stream()
                .map(SubCategory::getSubCategoryId)
                .collect(Collectors.toList());
        bookDTO.setSubcategoryIds(subcategoryIds);

        List<Integer> imageIds = book.getImages().stream()
                .map(Image::getImgId)
                .collect(Collectors.toList());
        bookDTO.setImageIds(imageIds);

        return bookDTO;
    }

    private static  String handlePrice(double price, int discount){
        double percent = (double)discount/100;

        double finalPrice = price * (1 - percent);

        int resultPrice = (int)Math.round(finalPrice);

        String result = resultPrice + "";

        return result;
    }
}
