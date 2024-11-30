package com.surya.easyshop.service;

import com.surya.easyshop.dto.ImageDto;
import com.surya.easyshop.dto.ProductDto;
import com.surya.easyshop.exception.AlreadyExistsException;
import com.surya.easyshop.exception.ResourceNotFoundException;
import com.surya.easyshop.model.Category;
import com.surya.easyshop.model.Image;
import com.surya.easyshop.model.Product;
import com.surya.easyshop.repository.CategoryRepository;
import com.surya.easyshop.repository.ImageRepository;
import com.surya.easyshop.repository.ProductRepository;
import com.surya.easyshop.request.AddProductRequest;
import com.surya.easyshop.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor //only pick up  non-static final ka?
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    private final ImageRepository imageRepository;


    @Override
    public Product addProduct(AddProductRequest request) {
        //check cat there or not

        // true use it else save new cat and use it to save product

        if(productExists(request.getName() , request.getBrand())){
            throw new AlreadyExistsException(request.getBrand() + " "+ request.getName()+ " already exits");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);


        return productRepository.save(createProduct(request ,category));
    }

    private boolean productExists(String name , String brand)
    {
        return productRepository.existsByNameAndBrand(name ,brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {throw new ResourceNotFoundException("Product not found!");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest product, Long productId) {


        return productRepository.findById(productId)
                .map(existingProduct-> updateExistingProduct(existingProduct , product))
                .map(updatedProduct -> productRepository.save(updatedProduct))
                .orElseThrow(()-> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct , ProductUpdateRequest request)
    {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setName(request.getName());
        existingProduct.setInventory(request.getInventory());


        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);


        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category , brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand , name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand , name);
    }

    @Override
    public  List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(p -> convretToDto(p)).collect(Collectors.toList());
    }


    @Override
    public ProductDto convretToDto(Product product){
        ProductDto productDto = modelMapper.map(product , ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = new ArrayList<>();
        for(Image image : images)
        {
            imageDtos.add(modelMapper.map(image , ImageDto.class));
        }

        productDto.setImages(imageDtos);

        return productDto;
    }
}
