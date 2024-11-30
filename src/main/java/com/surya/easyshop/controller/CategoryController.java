package com.surya.easyshop.controller;


import com.surya.easyshop.exception.AlreadyExistsException;
import com.surya.easyshop.exception.ResourceNotFoundException;
import com.surya.easyshop.model.Category;
import com.surya.easyshop.response.ApiResponse;
import com.surya.easyshop.service.CategoryService;
import com.surya.easyshop.service.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories()
    {
        try {
            List<Category> categoryList = categoryService.getAllCategories();

            return ResponseEntity.ok(new ApiResponse( "Found!" , categoryList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " , INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name){
        try {
            Category tempCategory = categoryService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Success" , tempCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage() , null));
        }
    }


    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category theCategory = categoryService.getCategoryById(id);

            return ResponseEntity.ok(new ApiResponse( "Found!" , theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }


    @GetMapping("/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category theCategory = categoryService.getCategoryByName(name);

            return ResponseEntity.ok(new ApiResponse( "Found!" , theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategoryByName(@PathVariable Long id){
        try {
            categoryService.deleteCategoryById(id);

            return ResponseEntity.ok(new ApiResponse( "Deleted!" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }


    @PutMapping("/category/{id}/update")
    public  ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id , @RequestBody Category category)
    {
        try {
            Category updatedCategory = categoryService.updateCategory(category , id);
            return ResponseEntity.ok(new ApiResponse("Update Success!" , updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }

    }




}
