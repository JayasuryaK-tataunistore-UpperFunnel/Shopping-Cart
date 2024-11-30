package com.surya.easyshop.controller;


import com.surya.easyshop.dto.ProductDto;
import com.surya.easyshop.exception.AlreadyExistsException;
import com.surya.easyshop.exception.ResourceNotFoundException;
import com.surya.easyshop.model.Product;
import com.surya.easyshop.request.AddProductRequest;
import com.surya.easyshop.request.ProductUpdateRequest;
import com.surya.easyshop.response.ApiResponse;
import com.surya.easyshop.service.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("success" , productDtos));
    }


    @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId)
    {
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convretToDto(product);
            return ResponseEntity.ok(new ApiResponse("success" , productDto));
        }  catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            Product theProduct = productService.addProduct(product);
            ProductDto productDto = productService.convretToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Add Product Success" , productDto));
        } catch (AlreadyExistsException e) {
            return  ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage() , null));
        }

    }


    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request , @PathVariable Long productId)
    {
        try {
            Product product = productService.updateProduct(request , productId);

            return ResponseEntity.ok(new ApiResponse("Update Product Success" , product));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id)
    {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Delete Product Success" , id));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }

    }



    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand , @RequestParam  String name)
    {

        try {
            List<Product> products = productService.getProductsByBrandAndName(brand , name);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            if(products.isEmpty())
            {
                return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found with name:" , name));
            }
            return ResponseEntity.ok(new ApiResponse("success" , productDtos));
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }

    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category , @RequestParam  String brand)
    {

        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category , brand);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            if(products.isEmpty())
            {
                return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found " , null));
            }
            return ResponseEntity.ok(new ApiResponse("success" , productDtos));
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }

    }

    @GetMapping("products/{name}/product")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name)
    {
        try {
            List<Product> products = productService.getProductsByName(name);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            if(products.isEmpty())
            {
                return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found " , null));
            }
            return ResponseEntity.ok(new ApiResponse("success" , productDtos));
        }  catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand)
    {

        try {
            List<Product> products = productService.getProductsByBrand(brand);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            if(products.isEmpty())
            {
                return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products Found " , null));
            }
            return ResponseEntity.ok(new ApiResponse("success" , productDtos));
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }

    }


    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }

            return  ResponseEntity.ok(new ApiResponse("success", productDtos));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }




}
