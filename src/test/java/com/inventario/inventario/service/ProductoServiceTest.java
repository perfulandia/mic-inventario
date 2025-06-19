package com.inventario.inventario.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inventario.inventario.model.Producto;
import com.inventario.inventario.repository.ProductoRepository;

public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Producto p1 = new Producto(null, "Playstation 2", 20000L, 32, "Sony");
        Producto p2 = new Producto(null, "Xbox One", 15000L, 64, "Microsoft");
        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = productoService.findAll();
        assertThat(resultado).hasSize(2).contains(p1, p2);
        verify(productoRepository).findAll();
    }

    @Test
    void testFindById() {
        Producto producto = new Producto(1L, "Playstation 2", 20000L, 32, "Sony");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1L).get();

        assertThat(resultado.getId()).isEqualTo(1L);

        verify(productoRepository).findById(1L);
    }

    @Test
    public void testFindAllById() {
        Producto p1 = new Producto(1L, "Xbox Series X", 45000L, 15, "Microsoft");
        Producto p2 = new Producto(2L, "PlayStation 5", 50000L, 20, "Sony");

        List<Long> ids = Arrays.asList(1L, 2L);
        List<Producto> mockProductos = Arrays.asList(p1, p2);

        when(productoRepository.findAllById(ids)).thenReturn(mockProductos);

        List<Producto> foundProductos = productoService.findAllById(ids);

        assertEquals(2, foundProductos.size());
        assertTrue(foundProductos.containsAll(mockProductos));
        verify(productoRepository).findAllById(ids);
    }

    // EXISTS BY ID
    @Test
    void testExistsById(){
        Long idExistente = 1L;
        when(productoRepository.existsById(idExistente)).thenReturn(true);

        boolean resultado = productoService.existsById(idExistente);
        
        assertTrue(resultado);
        verify(productoRepository).existsById(idExistente);
    }

    @Test
    void testSave() {
        Producto producto = new Producto(null, "Playstation 2", 20000L, 32, "Sony");
        Producto productoGuardado = new Producto(1L, "Playstation 2", 20000L, 32, "Sony");

        when(productoRepository.save(producto)).thenReturn(productoGuardado);

        Producto resultado = productoService.save(producto);

        assertThat(resultado.getId()).isEqualTo(1L);

        verify(productoRepository).save(producto);
    }

    @Test
    void testUpdate() {
        Producto p1 = new Producto(1L, "Playstation 2", 20000L, 32, "Sony");
        Producto p2 = new Producto(1L, "Xbox One", 15000L, 64, "Microsoft");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p1));

        productoService.update(1L, p2);

        verify(productoRepository).save(p2);
    }

    @Test
    public void testDeleteById() {
        Producto existingProduct = new Producto(1L, "Xbox One", 15000L, 64, "Microsoft");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        productoService.deleteById(1L);

        verify(productoRepository).deleteById(1L);
    }

}