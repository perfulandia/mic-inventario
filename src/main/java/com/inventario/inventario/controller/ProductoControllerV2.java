package com.inventario.inventario.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.inventario.inventario.assemblers.ProductoModelAssembler;
import com.inventario.inventario.model.Producto;
import com.inventario.inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequestMapping("/api/v2/productos")
@RestController
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler productoAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getProductos() {
        List<Producto> productos = productoService.findAll();

        if (!productos.isEmpty()) {
            List<EntityModel<Producto>> productResources = productos.stream()
                    .map(productoAssembler::toModel)
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productResources,
                    linkTo(methodOn(ProductoControllerV2.class).getProductos()).withSelfRel(),
                    linkTo(methodOn(ProductoControllerV2.class).saveProducto(null)).withRel("create"));

            return new ResponseEntity<>(collectionModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // obtener producto segun su id
    @GetMapping("/id/{id}")
    public ResponseEntity<EntityModel<Producto>> findProducto(@PathVariable Long id) {
        return productoService.findById(id)
                .map(productoAssembler::toModel)
                .map(resource -> new ResponseEntity<>(resource, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // obtener una lista de productos pasando sus id's
    @GetMapping("/by-id/")
    public ResponseEntity<List<Producto>> getProductosById(@RequestParam List<Long> ids) {
        return new ResponseEntity<>(productoService.findAllById(ids), HttpStatus.OK);
    }

    // guardar producto si el id no esta utilizado
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> saveProducto(@RequestBody Producto producto) {
        if (!productoService.existsById(producto.getId())) {
            Producto savedProducto = productoService.save(producto);
            return new ResponseEntity<>(productoAssembler.toModel(savedProducto), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    // actualizar producto por id
    @PutMapping("/id/{id}")
    public ResponseEntity<EntityModel<Producto>> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        if (productoService.existsById(id)) {
            Producto updatedProducto = productoService.update(id, producto);
            return new ResponseEntity<>(productoAssembler.toModel(updatedProducto), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // eliminar producto por id (borrado logico)
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.existsById(id)) {
            productoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
