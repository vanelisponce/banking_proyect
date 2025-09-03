package com.banking.cliente.event;

import java.io.Serializable;

public class ClienteCreatedEvent implements Serializable {
    private Long clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;

    public ClienteCreatedEvent() {}

    public ClienteCreatedEvent(Long clienteId, String nombre, String identificacion, Boolean estado) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}