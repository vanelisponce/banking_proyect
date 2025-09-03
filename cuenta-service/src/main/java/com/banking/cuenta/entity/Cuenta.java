package com.banking.cuenta.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuentas")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuenta_id")
    private Long cuentaId;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private String tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoInicial;

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos = new ArrayList<>();

    // Constructores
    public Cuenta() {}

    public Cuenta(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial,
                  Boolean estado, Long clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.clienteId = clienteId;
    }

    // Getters y Setters
    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }
    
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    
    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }
    
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public List<Movimiento> getMovimientos() { return movimientos; }
    public void setMovimientos(List<Movimiento> movimientos) { this.movimientos = movimientos; }
}