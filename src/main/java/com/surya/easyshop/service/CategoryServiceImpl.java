package com.surya.easyshop.service;

import com.surya.easyshop.exception.AlreadyExistsException;
import com.surya.easyshop.exception.ResourceNotFoundException;
import com.surya.easyshop.model.Category;
import com.surya.easyshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long Id) {
        return categoryRepository.findById(Id).orElseThrow(()-> new ResourceNotFoundException("Category Not Found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c-> !categoryRepository.existsByName(c.getName()))
                .map(c-> categoryRepository.save(c))
                .orElseThrow(()-> new AlreadyExistsException(category.getName() + " Already Exists!"));
    }

    @Override
    public Category updateCategory(Category category , Long id) {


        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(()-> new ResourceNotFoundException("Category Not Found!"));

    }

    @Override
    public void deleteCategoryById(Long id) {

        categoryRepository.findById(id).ifPresentOrElse(category -> categoryRepository.delete(category) , ()-> new ResourceNotFoundException("Category Not Found!"));
    }
}
