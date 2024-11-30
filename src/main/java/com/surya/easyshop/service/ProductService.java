package com.surya.easyshop.service;

import com.surya.easyshop.dto.ProductDto;
import com.surya.easyshop.model.Product;
import com.surya.easyshop.request.AddProductRequest;
import com.surya.easyshop.request.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    Product addProduct(AddProductRequest product);


    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(ProductUpdateRequest product , Long productId);


    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category , String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand , String name);

    Long countProductsByBrandAndName(String brand , String name);

    List<ProductDto> getConvertedProducts(List<Product> products);


    ProductDto convretToDto(Product product);
}
