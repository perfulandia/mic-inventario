package com.inventario.inventario.controller;

import java.util.List;
import com.inventario.inventario.model.Producto;
import com.inventario.inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/productos")
@RestController

public class ProductoController {
    @Autowired
    private ProductoService productoService;

    // obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> getProductos() {
        List<Producto> productos = productoService.findAll();

        if (!productos.isEmpty()) {
            return new ResponseEntity<>(productos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // obtener producto segun su id
    @GetMapping("/id/{id}")
    public ResponseEntity<Producto> findProducto(@PathVariable Long id) {

        if (productoService.existsById(id)) {
            return new ResponseEntity<>(productoService.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // obtener una lista de productos pasando sus id's
    @GetMapping("/by-id/")
    public ResponseEntity<List<Producto>> getProductosById(@RequestParam List<Long> ids) {
        return new ResponseEntity<>(productoService.findAllById(ids), HttpStatus.OK);
    }

    // guardar producto si el id no esta utilizado
    @PostMapping
    public ResponseEntity<Producto> saveProducto(@RequestBody Producto producto) {
        if (!productoService.existsById(producto.getId())) {
            return new ResponseEntity<>(productoService.save(producto), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    // actualizar producto por id
    @PutMapping("/id/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {

        if (productoService.existsById(id)) {
            return new ResponseEntity<>(productoService.update(id, producto), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // eliminar producto por id
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.existsById(id)) {
            productoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
