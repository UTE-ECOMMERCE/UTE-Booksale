package com.ecommerce.booksale.book;

import com.ecommerce.booksale.book.category.Category;
import com.ecommerce.booksale.book.subcategory.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, PagingAndSortingRepository<Book, Integer> {
    // find All Books
    List<Book> findAll();

    Page<Book> findAll(Pageable pageable);

    Page<Book> findByCategories(Category category, Pageable pageable);

    Page<Book> findBySubcategories(SubCategory subCategory, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

}
