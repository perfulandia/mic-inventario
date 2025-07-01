package com.inventario.inventario.controller;

import com.inventario.inventario.model.Producto;
import com.inventario.inventario.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper; // Import ObjectMapper for JSON conversion
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProductoService productoService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testGetProductos_ReturnsOkWhenProductsExist() throws Exception {

                List<Producto> productos = Arrays.asList(
                                new Producto(1L, true, "Playstation 3", 1200L, 10, "Sony"),
                                new Producto(2L, true, "Xbox 360", 2500L, 50, "Microsoft"));
                when(productoService.findAll()).thenReturn(productos);

                mockMvc.perform(get("/api/v1/productos")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].nombre", is("Playstation 3")))
                                .andExpect(jsonPath("$[1].nombre", is("Xbox 360")));

                verify(productoService, times(1)).findAll();
        }

        @Test
        void testGetProductos_ReturnsNoContentWhenNoProducts() throws Exception {

                List<Producto> productos = Arrays.asList();
                when(productoService.findAll()).thenReturn(productos);

                mockMvc.perform(get("/api/v1/productos")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent())
                                .andExpect(content().string(""));

                verify(productoService, times(1)).findAll();
        }

        @Test
        void testFindProducto_ReturnsOkWhenProductExists() throws Exception {
                Long productId = 1L;
                Producto producto = new Producto(productId, true, "Playstation 3", 1200L, 10, "Sony");
                when(productoService.existsById(productId)).thenReturn(true);
                when(productoService.findById(productId)).thenReturn(Optional.of(producto));

                mockMvc.perform(get("/api/v1/productos/id/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(productId.intValue())))
                                .andExpect(jsonPath("$.nombre", is("Playstation 3")));

                verify(productoService, times(1)).existsById(productId);
                verify(productoService, times(1)).findById(productId);
        }

        @Test
        void testFindProducto_ReturnsNotFoundWhenProductDoesNotExist() throws Exception {

                Long productId = 1L;
                when(productoService.existsById(productId)).thenReturn(false);

                mockMvc.perform(get("/api/v1/productos/id/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(productoService, times(1)).existsById(productId);
                verify(productoService, times(0)).findById(anyLong());
        }

        @Test
        void testGetProductosById_ReturnsOk() throws Exception {

                List<Long> productIds = Arrays.asList(1L, 2L);
                List<Producto> productos = Arrays.asList(
                                new Producto(1L, true, "Playstation 3", 1200L, 10, "Sony"),
                                new Producto(2L, true, "Xbox 360", 25L, 50, "Microsoft"));
                when(productoService.findAllById(anyList())).thenReturn(productos);

                mockMvc.perform(get("/api/v1/productos/by-id/")
                                .param("ids", "1", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[1].id", is(2)));

                verify(productoService, times(1)).findAllById(productIds);
        }

        @Test
        void testSaveProducto_ReturnsOkWhenProductDoesNotExist() throws Exception {

                Producto newProducto = new Producto(3L, true, "Keyboard", 75L, 20, "Razer");
                when(productoService.existsById(newProducto.getId())).thenReturn(false);
                when(productoService.save(any(Producto.class))).thenReturn(newProducto);

                mockMvc.perform(post("/api/v1/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newProducto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(newProducto.getId().intValue())))
                                .andExpect(jsonPath("$.nombre", is(newProducto.getNombre())));

                verify(productoService, times(1)).existsById(newProducto.getId());
                verify(productoService, times(1)).save(any(Producto.class));
        }

        @Test
        void testSaveProducto_ReturnsConflictWhenProductExists() throws Exception {

                Producto existingProducto = new Producto(1L, true, "Playstation 3", 1200L, 10, "Sony");
                when(productoService.existsById(existingProducto.getId())).thenReturn(true);

                mockMvc.perform(post("/api/v1/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(existingProducto)))
                                .andExpect(status().isConflict());

                verify(productoService, times(1)).existsById(existingProducto.getId());
                verify(productoService, times(0)).save(any(Producto.class));
        }

        @Test
        void testUpdateProducto_ReturnsOkWhenProductExists() throws Exception {

                Long productId = 1L;
                Producto updatedProducto = new Producto(productId, true, "Playstation 3 Pro", 1500L, 15, "Sony");
                when(productoService.existsById(productId)).thenReturn(true);
                when(productoService.update(anyLong(), any(Producto.class))).thenReturn(updatedProducto);

                mockMvc.perform(put("/api/v1/productos/id/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProducto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(updatedProducto.getId().intValue())))
                                .andExpect(jsonPath("$.nombre", is(updatedProducto.getNombre())));

                verify(productoService, times(1)).existsById(productId);
                verify(productoService, times(1)).update(anyLong(), any(Producto.class));
        }

        @Test
        void testUpdateProducto_ReturnsNotFoundWhenProductDoesNotExist() throws Exception {

                Long productId = 1L;
                Producto updatedProducto = new Producto(productId, true, "Playstation 3 Pro", 1500L, 15, "Sony");
                when(productoService.existsById(productId)).thenReturn(false);

                mockMvc.perform(put("/api/v1/productos/id/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProducto)))
                                .andExpect(status().isNotFound());

                verify(productoService, times(1)).existsById(productId);
                verify(productoService, times(0)).update(anyLong(), any(Producto.class));
        }

        @Test
        void testDeleteProducto_ReturnsNoContentWhenProductExists() throws Exception {

                Long productId = 1L;
                when(productoService.existsById(productId)).thenReturn(true);
                doNothing().when(productoService).deleteById(productId);

                mockMvc.perform(delete("/api/v1/productos/id/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

                verify(productoService, times(1)).existsById(productId);
                verify(productoService, times(1)).deleteById(productId);
        }

        @Test
        void testDeleteProducto_ReturnsNotFoundWhenProductDoesNotExist() throws Exception {

                Long productId = 1L;
                when(productoService.existsById(productId)).thenReturn(false);

                mockMvc.perform(delete("/api/v1/productos/id/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(productoService, times(1)).existsById(productId);
                verify(productoService, times(0)).deleteById(anyLong());
        }
}
