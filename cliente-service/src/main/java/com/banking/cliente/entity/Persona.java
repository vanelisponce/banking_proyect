package com.banking.cliente.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long personaId;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 1)
    private String genero;
    
    @Column(nullable = false)
    private Integer edad;
    
    @Column(nullable = false, unique = true, length = 20)
    private String identificacion;
    
    @Column(length = 200)
    private String direccion;
    
    @Column(length = 15)
    private String telefono;

    // Constructores
    public Persona() {}

    public Persona(String nombre, String genero, Integer edad, String identificacion,
                   String direccion, String telefono) {
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getPersonaId() { return personaId; }
    public void setPersonaId(Long personaId) { this.personaId = personaId; }
    
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
}