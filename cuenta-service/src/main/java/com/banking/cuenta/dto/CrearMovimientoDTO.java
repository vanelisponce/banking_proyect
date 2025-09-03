package com.banking.cuenta.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CrearMovimientoDTO {
    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor;

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}