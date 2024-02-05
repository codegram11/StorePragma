package com.pragma.api.domain.payload;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@Builder
public class MensajeResponse<T>  {

    private String message;
    private T object;

}