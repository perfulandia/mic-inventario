package com.inventario.inventario.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.inventario.inventario.controller.ProductoControllerV2;
import com.inventario.inventario.model.Producto;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        EntityModel<Producto> resource = EntityModel.of(producto);

        resource.add(linkTo(methodOn(ProductoControllerV2.class).findProducto(producto.getId())).withSelfRel());

        resource.add(
                linkTo(methodOn(ProductoControllerV2.class).updateProducto(producto.getId(), null)).withRel("update"));

        resource.add(linkTo(methodOn(ProductoControllerV2.class).deleteProducto(producto.getId())).withRel("delete"));

        resource.add(linkTo(methodOn(ProductoControllerV2.class).getProductos()).withRel("all-products"));

        return resource;
    }
}
