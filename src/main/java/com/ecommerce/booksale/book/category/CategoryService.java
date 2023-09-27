package com.ecommerce.booksale.book.category;


import com.ecommerce.booksale.exception.BookNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable("categories")
    public List<CategoryDTO> getAllCategories() {



        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoriesDTO = categories.stream()
                                                    .map(CategoryMapper::toDTO)
                                                    .collect(Collectors.toList());
        return categoriesDTO;
    }

    public Map<String, CategoryDTO> getHomeCategories(){

        try{
            // create new Map
            Map<String, CategoryDTO> categoryMap = new HashMap<>();

            // get categories
            Category fiction = categoryRepository.findById(1)
                    .orElseThrow(() -> new BookNotFoundException("Không có mục sách tiểu thuyết")); // id = 1 is fiction book in database
            CategoryDTO fictionDTO = CategoryMapper.toDTO(fiction);
            Category selfHelp = categoryRepository.findById(6)
                    .orElseThrow(() -> new BookNotFoundException("Không có mục sách self-help")); // id = 6 is self-help book
            CategoryDTO selfHelpDTO = CategoryMapper.toDTO(selfHelp);
            Category children = categoryRepository.findById(7)
                    .orElseThrow(() -> new BookNotFoundException("Không có mục sách thiếu nhi")); // id = 7 is children book in database
            CategoryDTO childrenDTO = CategoryMapper.toDTO(children);

            // add to map
            categoryMap.put("fiction", fictionDTO);
            categoryMap.put("selfHelp", selfHelpDTO);
            categoryMap.put("children", childrenDTO);

            // return result
            return categoryMap;
        }catch (Exception ex){
            ex.getStackTrace();
            throw ex;
        }

    }

    public Category getCategoryById(int id){
        return categoryRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Không có danh mục này"));
    }







}
