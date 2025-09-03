package com.banking.cuenta.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CrearCuentaDTO {
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 20, message = "El número de cuenta no puede exceder 20 caracteres")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Size(max = 20, message = "El tipo de cuenta no puede exceder 20 caracteres")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial debe ser mayor o igual a 0")
    private BigDecimal saldoInicial;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    
    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }
    
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}