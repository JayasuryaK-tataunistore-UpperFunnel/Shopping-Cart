package com.surya.easyshop.service;

import com.surya.easyshop.model.Category;

import java.util.List;

public interface CategoryService {

    Category getCategoryById(Long Id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category ,Long id);

    void deleteCategoryById(Long id);


}
