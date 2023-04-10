package com.alura.jdbc.modelo;

public class producto {

  private Integer id;
  private String nombre;
  private String descripcion;
  private Integer cantidad;
  
  // CONSTR
  public producto(String nombre, String descripcion, Integer cantidad) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.cantidad = cantidad;
  }

  
  
}
