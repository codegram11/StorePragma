package com.pragma.api.mapper;

import com.pragma.api.domain.dto.ProductDTO;
import com.pragma.api.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DataMaper {

    DataMaper INSTANCE = Mappers.getMapper(DataMaper.class);

    ProductDTO maptoDTO(Product product);

    Product maptoProduct(ProductDTO productDTO);

    List<ProductDTO> mapToDTOList(List<Product> productList);

    List<Product> mapToProductList(List<ProductDTO> productDTOList);
}
