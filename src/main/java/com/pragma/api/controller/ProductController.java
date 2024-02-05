package com.pragma.api.controller;

import com.pragma.api.domain.dto.ProductDTO;
import com.pragma.api.domain.payload.MensajeResponse;
import com.pragma.api.service.ProductService;
import com.pragma.api.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pragma")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("products")
    public ResponseEntity<MensajeResponse<List<ProductDTO>>> showAll() {
        log.info(Constants.LOG_INITIAL + "SHOW ALL - CONTROLLER");
        try {
            List<ProductDTO> productList = productService.listAll();

            if (productList == null) {
                return new ResponseEntity<>(
                        MensajeResponse.<List<ProductDTO>>builder()
                                .message(Constants.ERROR_PRODUCT)
                                .object(null)
                                .build(),
                        HttpStatus.OK);
            }

            return new ResponseEntity<>(
                    MensajeResponse.<List<ProductDTO>>builder()
                            .message(Constants.PRODUCT_LIST)
                            .object(productList)
                            .build(),
                    HttpStatus.OK);
        }catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.<List<ProductDTO>>builder()
                            .message(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PostMapping("product")
    public ResponseEntity<MensajeResponse<ProductDTO>> create(@RequestBody ProductDTO productDTO) {
        log.info(Constants.LOG_INITIAL + "CREATE PRODUCT");
        ProductDTO productSave = null;
        try {
            productSave = productService.save(productDTO);
            return new ResponseEntity<>(
                    MensajeResponse.<ProductDTO>builder()
                    .message(Constants.PRODUCT_SAVE)
                    .object(ProductDTO.builder()
                            .id(productSave.getId())
                            .title(productSave.getTitle())
                            .description(productSave.getDescription())
                            .imagePath(productSave.getImagePath())
                            .price(productSave.getPrice())
                            .build())
                    .build()
                    , HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.<ProductDTO>builder()
                            .message(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PutMapping("product/{id}")
    public ResponseEntity<MensajeResponse<ProductDTO>> update(@RequestBody ProductDTO productDTO, @PathVariable Integer id) {
        try {
            Optional<ProductDTO> existingProduct = productService.findById(id);

            if (existingProduct.isPresent()) {
                existingProduct.get().setId(id);
                existingProduct.get().setTitle(productDTO.getTitle());
                existingProduct.get().setDescription(productDTO.getDescription());
                existingProduct.get().setImagePath(productDTO.getImagePath());
                existingProduct.get().setPrice(productDTO.getPrice());
                ProductDTO updatedProduct = productService.save(existingProduct.get());
                return new ResponseEntity<>(
                        MensajeResponse.<ProductDTO>builder()
                        .message(Constants.UPDATA_DATA)
                        .object(updatedProduct)
                        .build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        MensajeResponse.<ProductDTO>builder()
                                .message(Constants.DATA_NOT_FOUND)
                                .object(null)
                                .build(), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.<ProductDTO>builder()
                            .message(exDt.getMessage())
                            .object(null)
                            .build(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<MensajeResponse<ProductDTO>> delete(@PathVariable Integer id) {
        try {
            Optional<ProductDTO> productDelete = productService.findById(id);
            if (productDelete.isPresent()) {
                productService.delete(productDelete.get());
                return new ResponseEntity<>(
                        MensajeResponse.<ProductDTO>builder()
                                .message(Constants.DELETE_DATA)
                                .object(ProductDTO.builder()
                                        .title(productDelete.get().getTitle())
                                        .description(productDelete.get().getDescription())
                                        .imagePath(productDelete.get().getImagePath())
                                        .price(productDelete.get().getPrice())
                                        .build())
                                .build()
                        , HttpStatus.OK);

            } else {
                return new ResponseEntity<>(
                        MensajeResponse.<ProductDTO>builder()
                                .message(Constants.DATA_DELETE_NOT_FOUND)
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND);
            }
        } catch (Exception exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.<ProductDTO>builder()
                            .message(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("product/{id}")
    public ResponseEntity<MensajeResponse<ProductDTO>> showById(@PathVariable Integer id) {
        Optional<ProductDTO> product = productService.findById(id);

        if (product.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.<ProductDTO>builder()
                            .message(Constants.FIND_DATA_ERROR)
                            .object(null)
                            .build()
                    , HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                MensajeResponse.<ProductDTO>builder()
                        .message(Constants.FIND_DATA)
                        .object(product.get())
                        .build()
                , HttpStatus.OK);
    }

}
