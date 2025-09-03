package com.banking.cuenta.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "movimientos")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long movimientoId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha = new Date();

    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    // Constructores
    public Movimiento() {}

    public Movimiento(Date fecha, String tipoMovimiento, BigDecimal valor,
                      BigDecimal saldo, Cuenta cuenta) {
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.cuenta = cuenta;
    }

    // Getters y Setters
    public Long getMovimientoId() { return movimientoId; }
    public void setMovimientoId(Long movimientoId) { this.movimientoId = movimientoId; }
    
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    
    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }
}