package com.banking.cliente.dto;

import jakarta.validation.constraints.*;

public class CrearClienteDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    @Size(max = 1, message = "El género debe ser un carácter")
    private String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 120, message = "La edad máxima es 120 años")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 20, message = "La identificación no puede exceder 20 caracteres")
    private String identificacion;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;

    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    private String telefono;

    // ❌ ELIMINAR ESTE CAMPO - No pedimos clienteId
    // @NotNull(message = "El clienteId es obligatorio")
    // private Long clienteId;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contraseña;

    // Getters y Setters (ELIMINAR getters/setters de clienteId)
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
}