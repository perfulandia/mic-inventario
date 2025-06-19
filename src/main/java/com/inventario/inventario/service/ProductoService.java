package com.inventario.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.inventario.model.Producto;
import com.inventario.inventario.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    public List<Producto> findAll(){
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id){
        return productoRepository.findById(id);
    }

    public List<Producto> findAllById(List<Long> ids){
        return productoRepository.findAllById(ids);
    }

    public Boolean existsById(Long id){
        return productoRepository.existsById(id);
    }

    public Producto save(Producto producto){
        return productoRepository.save(producto);
    }

    public Producto update(Long id, Producto producto){
        Producto holder = productoRepository.findById(id).get();

        if (producto != null){
            holder.setNombre(producto.getNombre());
            holder.setPrecio(producto.getPrecio());
            holder.setStock(producto.getStock());
            holder.setMarca(producto.getMarca());

            return productoRepository.save(holder);
        }
        return null;

    }

    public void deleteById(Long id){
        productoRepository.deleteById(id);
    }
}
