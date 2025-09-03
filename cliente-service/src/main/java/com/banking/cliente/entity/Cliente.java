package com.banking.cliente.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Cliente extends Persona {
    
    @Column(name = "cliente_id", unique = true, nullable = false)
    private Long clienteId;
    
    @Column(nullable = false)
    private String contraseña;
    
    @Column(nullable = false)
    private Boolean estado = true;

    // Constructores
    public Cliente() {
        super();
    }

    public Cliente(String nombre, String genero, Integer edad, String identificacion,
                   String direccion, String telefono, Long clienteId, String contraseña, Boolean estado) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contraseña = contraseña;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}