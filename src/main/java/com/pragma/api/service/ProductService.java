package com.pragma.api.service;

import com.pragma.api.domain.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {


    List<ProductDTO> listAll();

    ProductDTO save(ProductDTO product);


    Optional<ProductDTO> findById(Integer id);

    void delete(ProductDTO product);

    boolean existsById(Integer id);
}
