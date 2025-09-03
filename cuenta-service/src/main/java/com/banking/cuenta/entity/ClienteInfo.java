package com.banking.cuenta.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cliente_info")
public class ClienteInfo {
    @Id
    @Column(name = "cliente_id")
    private Long clienteId;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 20)
    private String identificacion;
    
    @Column(nullable = false)
    private Boolean estado;
    
    @Column(name = "fecha_sincronizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSincronizacion = new Date();

    // Constructores
    public ClienteInfo() {}

    // Getters y Setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    
    public Date getFechaSincronizacion() { return fechaSincronizacion; }
    public void setFechaSincronizacion(Date fechaSincronizacion) { this.fechaSincronizacion = fechaSincronizacion; }
}