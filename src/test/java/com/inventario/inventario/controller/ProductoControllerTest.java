package com.inventario.inventario.controller;

import com.inventario.inventario.model.Producto;
import com.inventario.inventario.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductos_ReturnsOkWhenProductsExist() {
        // Arrange
        List<Producto> productos = Arrays.asList(
                new Producto(1L, true, "Laptop", 1200L, 10, "Dell"),
                new Producto(2L, true, "Mouse", 25L, 50, "Logitech")
        );
        when(productoService.findAll()).thenReturn(productos);

        // Act
        ResponseEntity<List<Producto>> response = productoController.getProductos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productos, response.getBody());
        verify(productoService, times(1)).findAll();
    }

    @Test
    void testGetProductos_ReturnsNoContentWhenNoProducts() {
        // Arrange
        List<Producto> productos = Arrays.asList();
        when(productoService.findAll()).thenReturn(productos);

        // Act
        ResponseEntity<List<Producto>> response = productoController.getProductos();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody()); // Body should be null for NO_CONTENT
        verify(productoService, times(1)).findAll();
    }

    @Test
    void testFindProducto_ReturnsOkWhenProductExists() {
        // Arrange
        Long productId = 1L;
        Producto producto = new Producto(productId, true, "Laptop", 1200L, 10, "Dell");
        when(productoService.existsById(productId)).thenReturn(true);
        when(productoService.findById(productId)).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<Producto> response = productoController.findProducto(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
        verify(productoService, times(1)).existsById(productId);
        verify(productoService, times(1)).findById(productId);
    }

    @Test
    void testFindProducto_ReturnsNotFoundWhenProductDoesNotExist() {
        // Arrange
        Long productId = 1L;
        when(productoService.existsById(productId)).thenReturn(false);

        // Act
        ResponseEntity<Producto> response = productoController.findProducto(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(productoService, times(1)).existsById(productId);
        verify(productoService, never()).findById(anyLong()); // Should not call findById
    }

    @Test
    void testGetProductosById_ReturnsOk() {
        // Arrange
        List<Long> productIds = Arrays.asList(1L, 2L);
        List<Producto> productos = Arrays.asList(
                new Producto(1L, true, "Laptop", 1200L, 10, "Dell"),
                new Producto(2L, true, "Mouse", 25L, 50, "Logitech")
        );
        when(productoService.findAllById(productIds)).thenReturn(productos);

        // Act
        ResponseEntity<List<Producto>> response = productoController.getProductosById(productIds);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productos, response.getBody());
        verify(productoService, times(1)).findAllById(productIds);
    }

    @Test
    void testSaveProducto_ReturnsOkWhenProductDoesNotExist() {
        // Arrange
        Producto newProducto = new Producto(3L, true, "Keyboard", 75L, 20, "Razer");
        when(productoService.existsById(newProducto.getId())).thenReturn(false);
        when(productoService.save(newProducto)).thenReturn(newProducto);

        // Act
        ResponseEntity<Producto> response = productoController.saveProducto(newProducto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newProducto, response.getBody());
        verify(productoService, times(1)).existsById(newProducto.getId());
        verify(productoService, times(1)).save(newProducto);
    }

    @Test
    void testSaveProducto_ReturnsConflictWhenProductExists() {
        // Arrange
        Producto existingProducto = new Producto(1L, true, "Laptop", 1200L, 10, "Dell");
        when(productoService.existsById(existingProducto.getId())).thenReturn(true);

        // Act
        ResponseEntity<Producto> response = productoController.saveProducto(existingProducto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(productoService, times(1)).existsById(existingProducto.getId());
        verify(productoService, never()).save(any(Producto.class)); // Should not call save
    }

    @Test
    void testUpdateProducto_ReturnsOkWhenProductExists() {
        // Arrange
        Long productId = 1L;
        Producto updatedProducto = new Producto(productId, true, "Laptop Pro", 1500L, 15, "Dell");
        when(productoService.existsById(productId)).thenReturn(true);
        when(productoService.update(productId, updatedProducto)).thenReturn(updatedProducto);

        // Act
        ResponseEntity<Producto> response = productoController.updateProducto(productId, updatedProducto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProducto, response.getBody());
        verify(productoService, times(1)).existsById(productId);
        verify(productoService, times(1)).update(productId, updatedProducto);
    }

    @Test
    void testUpdateProducto_ReturnsNotFoundWhenProductDoesNotExist() {
        // Arrange
        Long productId = 1L;
        Producto updatedProducto = new Producto(productId, true, "Laptop Pro", 1500L, 15, "Dell");
        when(productoService.existsById(productId)).thenReturn(false);

        // Act
        ResponseEntity<Producto> response = productoController.updateProducto(productId, updatedProducto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(productoService, times(1)).existsById(productId);
        verify(productoService, never()).update(anyLong(), any(Producto.class)); // Should not call update
    }

    @Test
    void testDeleteProducto_ReturnsNoContentWhenProductExists() {
        // Arrange
        Long productId = 1L;
        when(productoService.existsById(productId)).thenReturn(true);
        doNothing().when(productoService).deleteById(productId);

        // Act
        ResponseEntity<Void> response = productoController.deleteProducto(productId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(productoService, times(1)).existsById(productId);
        verify(productoService, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProducto_ReturnsNotFoundWhenProductDoesNotExist() {
        // Arrange
        Long productId = 1L;
        when(productoService.existsById(productId)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = productoController.deleteProducto(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(productoService, times(1)).existsById(productId);
        verify(productoService, never()).deleteById(anyLong()); // Should not call deleteById
    }
}
