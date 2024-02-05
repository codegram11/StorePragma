package com.pragma.api.domain.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigInteger;

@Builder
@Data
public class ProductDTO {
    private Integer id;
    private String imagePath;
    private String title;
    private String description;
    private BigInteger price;

}
