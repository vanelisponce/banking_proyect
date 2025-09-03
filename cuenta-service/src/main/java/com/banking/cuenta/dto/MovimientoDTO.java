package com.banking.cuenta.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MovimientoDTO {
    private Long movimientoId;
    private Date fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private String numeroCuenta;

    public MovimientoDTO() {}

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
    
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
}