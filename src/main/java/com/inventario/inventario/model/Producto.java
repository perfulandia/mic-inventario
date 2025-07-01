package com.inventario.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "producto")

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Boolean activo;
    
    @Column(length = 100, nullable = false, unique = true)
    private String nombre;

    @Column(length = 15, nullable = false)
    private Long precio;

    @Column(length = 10, nullable = false)
    private int stock;

    @Column(length = 100, nullable = false)
    private String marca;
}
