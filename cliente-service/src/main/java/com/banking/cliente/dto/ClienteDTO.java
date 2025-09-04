package com.banking.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(description = "DTO que representa la información de un cliente")
public class ClienteDTO {
    
    @Schema(
        description = "ID único del cliente en el sistema", 
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long clienteId;
    
    @Schema(
        description = "Nombre completo del cliente", 
        example = "Jose Lema"
    )
    private String nombre;
    
    @Schema(
        description = "Género del cliente", 
        example = "M",
        allowableValues = {"M", "F"}
    )
    private String genero;
    
    @Schema(
        description = "Edad del cliente en años", 
        example = "30",
        minimum = "18",
        maximum = "120"
    )
    private Integer edad;
    
    @Schema(
        description = "Número de identificación del cliente", 
        example = "1234567890"
    )
    private String identificacion;
    
    @Schema(
        description = "Dirección de residencia", 
        example = "Otavalo sn y principal"
    )
    private String direccion;
    
    @Schema(
        description = "Número de teléfono", 
        example = "098254785"
    )
    private String telefono;
    
    @Schema(
        description = "Estado del cliente (activo/inactivo)", 
        example = "true"
    )
    private Boolean estado;
    
    // Constructors
    public ClienteDTO() {}
    
    public ClienteDTO(Long clienteId, String nombre, String genero, Integer edad, 
                     String identificacion, String direccion, String telefono, Boolean estado) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = estado;
    }
    
    // Getters y Setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
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
    
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}