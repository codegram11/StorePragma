package com.pragma.api.controller;

import com.pragma.api.data.DataTest;
import com.pragma.api.domain.dto.ProductDTO;
import com.pragma.api.domain.payload.MensajeResponse;
import com.pragma.api.service.ProductService;
import com.pragma.api.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void showAll() {
        // Configuración de Mockito
        when(productService.listAll()).thenReturn(DataTest.listAll());

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<List<ProductDTO>>> responseEntity = productController.showAll();

        // Verificaciones
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.PRODUCT_LIST, responseEntity.getBody().getMessage());
        assertEquals(1, responseEntity.getBody().getObject().size());

    }

    @Test
    void showAll_DataAccessException() {
        // Configuración de Mockito para simular DataAccessException
        when(productService.listAll()).thenThrow(new DataAccessException("Simulated DataAccessException") {});

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<List<ProductDTO>>> responseEntity = productController.showAll();

        // Verificaciones
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        assertEquals("Simulated DataAccessException", responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getObject());
    }

    @Test
    void showAll_null() {
        // Configuración de Mockito para simular DataAccessException
        when(productService.listAll()).thenReturn(null);

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<List<ProductDTO>>> responseEntity = productController.showAll();

        // Verificaciones
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.ERROR_PRODUCT, responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getObject());
    }

    @Test
    void update_correcto() {
        // Configuración de Mockito
        Integer productId = 1;
        ProductDTO existingProductDTO = DataTest.productNew();
        when(productService.findById(productId)).thenReturn(Optional.of(existingProductDTO));
        when(productService.save(any(ProductDTO.class))).thenReturn(existingProductDTO);

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.update(DataTest.productNew(), productId);

        // Verificaciones
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.UPDATA_DATA, responseEntity.getBody().getMessage());
        assertEquals(existingProductDTO, responseEntity.getBody().getObject());
        verify(productService, times(1)).findById(productId);
        verify(productService, times(1)).save(any(ProductDTO.class));
    }


    @Test
    void update_NotFoundWhenProductNotExists() {
        // Configuración de Mockito
        Integer productId = 1;
        ProductDTO inputProductDTO = DataTest.productNew();
        when(productService.findById(productId)).thenReturn(Optional.empty());

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.update(inputProductDTO, productId);

        // Verificaciones
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(Constants.DATA_NOT_FOUND, responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getObject());
        verify(productService, times(1)).findById(productId);
        verify(productService, never()).save(any(ProductDTO.class));
    }

    @Test
    void update_DataAccessException() {
        // Configuración de Mockito para simular DataAccessException
        Integer productId = 1;
        ProductDTO inputProductDTO = DataTest.productNew();
        when(productService.findById(productId)).thenReturn(Optional.of(DataTest.productNew()));
        when(productService.save(any(ProductDTO.class))).thenThrow(new DataAccessException("Simulated DataAccessException") {});

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.update(inputProductDTO, productId);

        // Verificaciones
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        assertEquals("Simulated DataAccessException", responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getObject());
        verify(productService, times(1)).findById(productId);
        verify(productService, times(1)).save(any(ProductDTO.class));
    }


    @Test
    void create() {
        // Configuración de Mockito
        ProductDTO inputProductDTO = DataTest.productNew();
        ProductDTO savedProductDTO = DataTest.productNew();
        savedProductDTO.setId(1);  // Establece el ID simulado
        when(productService.save(any(ProductDTO.class))).thenReturn(savedProductDTO);

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.create(inputProductDTO);

        // Verificaciones
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(Constants.PRODUCT_SAVE, responseEntity.getBody().getMessage());
        assertEquals(savedProductDTO, responseEntity.getBody().getObject());
        verify(productService, times(1)).save(any(ProductDTO.class));
    }

    @Test
    void create_DataAccessException() {
        when(productService.save(any(ProductDTO.class))).thenThrow(new DataAccessException("Simulated DataAccessException") {});

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.create(DataTest.productNew());

        // Verificaciones
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        assertEquals("Simulated DataAccessException", responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getObject());
        verify(productService, times(1)).save(any(ProductDTO.class));
    }

    @Test
    void delete() {
        // Configuración de Mockito
        Integer productId = 1;
        ProductDTO productToDelete = DataTest.productNew();
        productToDelete.setId(productId);
        when(productService.findById(productId)).thenReturn(Optional.of(productToDelete));

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.delete(productId);

        // Verificaciones
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.DELETE_DATA, responseEntity.getBody().getMessage());
        verify(productService, times(1)).findById(productId);
        verify(productService, times(1)).delete(any(ProductDTO.class));

    }

    @Test
    void delete_shouldReturnNotFoundWhenProductNotExists() {
        // Configuración de Mockito
        Integer productId = 1;
        when(productService.findById(productId)).thenReturn(Optional.empty());

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.delete(productId);

        // Verificaciones
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(Constants.DATA_DELETE_NOT_FOUND, responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getObject());
        verify(productService, times(1)).findById(productId);
        verify(productService, never()).delete(any(ProductDTO.class));
    }

    @Test
    void delete_Exception() {
        // Configuración de Mockito para simular una excepción
        Integer productId = 1;
        when(productService.findById(productId)).thenThrow(new RuntimeException("Simulated Exception"));

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.delete(productId);

        // Verificaciones
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        assertEquals("Simulated Exception", responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getObject());
        verify(productService, times(1)).findById(productId);
        verify(productService, never()).delete(any(ProductDTO.class));
    }

    @Test
    void showById() {
        // Configuración de Mockito
        Integer productId = 1;
        ProductDTO existingProduct = DataTest.productExistente();
        existingProduct.setId(productId);
        when(productService.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.showById(productId);

        // Verificaciones
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.FIND_DATA, responseEntity.getBody().getMessage());
        assertEquals(existingProduct, responseEntity.getBody().getObject());
        verify(productService, times(1)).findById(productId);
    }

    @Test
    void showById_ReturnNotFoundWhenProductNotExists() {
        // Configuración de Mockito
        Integer productId = 1;
        when(productService.findById(productId)).thenReturn(Optional.empty());

        // Llamada al método del controlador
        ResponseEntity<MensajeResponse<ProductDTO>> responseEntity = productController.showById(productId);

        // Verificaciones
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(Constants.FIND_DATA_ERROR, responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getObject());
        verify(productService, times(1)).findById(productId);
    }
}