package com.surya.easyshop.service;

import com.surya.easyshop.dto.ImageDto;
import com.surya.easyshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImages(List<MultipartFile> files , Long productId);

    void updateImage(MultipartFile file , Long imageId);


}
