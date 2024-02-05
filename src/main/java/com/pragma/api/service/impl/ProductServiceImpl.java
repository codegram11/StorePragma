package com.pragma.api.service.impl;

import com.pragma.api.domain.dto.ProductDTO;
import com.pragma.api.domain.entity.Product;;
import com.pragma.api.mapper.DataMaper;
import com.pragma.api.repository.ProductRepository;

import com.pragma.api.service.ProductService;
import com.pragma.api.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> listAll() {
        log.info(Constants.LOG_INITIAL + "SHOW ALL - SERVICE");

        return DataMaper.INSTANCE.mapToDTOList((List<Product>) productRepository.findAll());
    }

    @Override
    public ProductDTO save(ProductDTO product) {
        return DataMaper.INSTANCE.maptoDTO(productRepository.save(DataMaper.INSTANCE.maptoProduct(product)));
    }


    @Override
    public Optional<ProductDTO> findById(Integer id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);

            return productOptional.map(DataMaper.INSTANCE::maptoDTO);
        } catch (DataAccessException exDt) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(ProductDTO product) {
        productRepository.delete(DataMaper.INSTANCE.maptoProduct(product));
    }

    @Override
    public boolean existsById(Integer id) {
        return productRepository.existsById(id);
    }
}
