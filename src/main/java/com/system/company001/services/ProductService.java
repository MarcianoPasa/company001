package com.system.company001.services;

import com.system.company001.dtos.ProductListRecordDto;
import com.system.company001.dtos.ProductRecordDto;
import com.system.company001.models.ProductModel;
import com.system.company001.repositories.ProductRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductListRecordDto> getAllProducts(Pageable pageable) {
        return productRepository.findAllProductListRecordDto(pageable);
    }

    public Optional<ProductModel> getOneProduct(UUID id) {
        return productRepository.findById(id);
    }

    @Transactional
    public ProductModel save(ProductRecordDto dto) {
        var product = dto.convertToProductModel();
        processImages(dto.image(), product);
        return productRepository.save(product);
    }

    @Transactional
    public Optional<ProductModel> update(UUID id, ProductRecordDto dto) {
        return productRepository.findById(id).map(product -> {
            product.setName(dto.name());
            product.setValue(dto.value());

            if (dto.image() != null && !dto.image().isBlank()) {
                if (!isImageSame(product, dto)) {
                    processImages(dto.image(), product);
                }
            } else {
                product.setImage(null);
                product.setThumbnail(null);
            }

            return productRepository.save(product);
        });
    }

    @Transactional
    public boolean deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isImageSame(ProductModel model, ProductRecordDto dto) {
        if (model.getImage() == null && dto.image() == null) {
            return true;
        }
        if (model.getImage() == null || dto.image() == null) {
            return false;
        }
        try {
            String base64Data = dto.image().contains(",") ? dto.image().split(",")[1] : dto.image();
            byte[] dtoBytes = Base64.getDecoder().decode(base64Data);
            return Arrays.equals(model.getImage(), dtoBytes);
        } catch (Exception _) {
            return false;
        }
    }

    private void processImages(String base64Image, ProductModel model) {
        if (base64Image == null || base64Image.isEmpty()) {
            return;
        }
        try {
            String cleanBase64 = base64Image.contains(",") ? base64Image.split(",")[1] : base64Image;
            byte[] imageBytes = Base64.getDecoder().decode(cleanBase64);
            model.setImage(imageBytes);
            model.setThumbnail(generateThumbnail(imageBytes));
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Erro ao decodificar imagem: ", e);
        }
    }

    private byte[] generateThumbnail(byte[] originalImage) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ByteArrayInputStream inputStream = new ByteArrayInputStream(originalImage)
        ) {
            Thumbnails.of(inputStream)
                    .size(150, 150)
                    .outputFormat("png")
                    .toOutputStream(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao gerar thumbnail: ", e);
            return new ByteArrayOutputStream().toByteArray();
        }
    }
}
