package com.pragma.api.data;

import com.pragma.api.domain.dto.ProductDTO;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class DataTest {


    public static List<ProductDTO> listAll() {

        ProductDTO dto=ProductDTO.builder()
                .id(1)
                .title("Producto de prueba")
                .description("Descripción de prueba")
                .imagePath("/images/test.jpg")
                .price(new BigInteger(String.valueOf(10)))
                .build();

        return Collections.singletonList(dto);
    }

    public static ProductDTO productNew() {

        return ProductDTO.builder()
                .id(1)
                .title("Producto de prueba update")
                .description("Descripción de prueba")
                .imagePath("/images/test.jpg")
                .price(new BigInteger(String.valueOf(10)))
                .build();

    }

    public static ProductDTO productExistente() {

        return ProductDTO.builder()
                .id(1)
                .title("Producto de prueba modificado")
                .description("Descripción de prueba")
                .imagePath("/images/test.jpg")
                .price(new BigInteger(String.valueOf(10)))
                .build();

    }
}
