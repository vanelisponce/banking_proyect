package com.banking.cuenta.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReporteEstadoCuentaDTO {
    private String cliente;
    private Date fechaInicio;
    private Date fechaFin;
    private List<CuentaReporteDTO> cuentas;

    public ReporteEstadoCuentaDTO() {}

    // Getters y Setters
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    
    public List<CuentaReporteDTO> getCuentas() { return cuentas; }
    public void setCuentas(List<CuentaReporteDTO> cuentas) { this.cuentas = cuentas; }

    public static class CuentaReporteDTO {
        private String numeroCuenta;
        private String tipo;
        private BigDecimal saldoInicial;
        private BigDecimal saldoDisponible;
        private List<MovimientoReporteDTO> movimientos;

        // Getters y Setters
        public String getNumeroCuenta() { return numeroCuenta; }
        public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public BigDecimal getSaldoInicial() { return saldoInicial; }
        public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }
        
        public BigDecimal getSaldoDisponible() { return saldoDisponible; }
        public void setSaldoDisponible(BigDecimal saldoDisponible) { this.saldoDisponible = saldoDisponible; }
        
        public List<MovimientoReporteDTO> getMovimientos() { return movimientos; }
        public void setMovimientos(List<MovimientoReporteDTO> movimientos) { this.movimientos = movimientos; }
    }

    public static class MovimientoReporteDTO {
        private Date fecha;
        private String tipoMovimiento;
        private BigDecimal valor;
        private BigDecimal saldo;

        // Getters y Setters
        public Date getFecha() { return fecha; }
        public void setFecha(Date fecha) { this.fecha = fecha; }
        
        public String getTipoMovimiento() { return tipoMovimiento; }
        public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
        
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
        
        public BigDecimal getSaldo() { return saldo; }
        public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    }
}