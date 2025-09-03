package com.banking.cuenta.service;

import com.banking.cuenta.dto.CrearMovimientoDTO;
import com.banking.cuenta.dto.MovimientoDTO;
import com.banking.cuenta.entity.Cuenta;
import com.banking.cuenta.entity.Movimiento;
import com.banking.cuenta.repository.CuentaRepository;
import com.banking.cuenta.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuenta;
    private CrearMovimientoDTO crearMovimientoDTO;
    private MovimientoDTO movimientoDTO;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setCuentaId(1L);
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
        cuenta.setEstado(true);
        cuenta.setClienteId(1L);
        cuenta.setMovimientos(new ArrayList<>());

        crearMovimientoDTO = new CrearMovimientoDTO();
        crearMovimientoDTO.setNumeroCuenta("478758");
        crearMovimientoDTO.setValor(new BigDecimal("-575.00")); // Retiro

        movimientoDTO = new MovimientoDTO();
        movimientoDTO.setTipoMovimiento("Retiro");
        movimientoDTO.setValor(new BigDecimal("-575.00"));
        movimientoDTO.setSaldo(new BigDecimal("1425.00"));
        movimientoDTO.setNumeroCuenta("478758");
    }

    @Test
    void crearMovimiento_RetiroValido_RetornaMovimientoDTO() {
        // Arrange
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findByCuentaCuentaId(1L)).thenReturn(new ArrayList<>());
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(new Movimiento());
        when(modelMapper.map(any(Movimiento.class), eq(MovimientoDTO.class))).thenReturn(movimientoDTO);

        // Act
        MovimientoDTO resultado = movimientoService.crearMovimiento(crearMovimientoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Retiro", resultado.getTipoMovimiento());
        assertEquals(new BigDecimal("-575.00"), resultado.getValor());
        verify(cuentaRepository, times(1)).findByNumeroCuenta("478758");
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void crearMovimiento_SaldoInsuficiente_LanzaExcepcion() {
        // Arrange
        crearMovimientoDTO.setValor(new BigDecimal("-2500.00")); // Más del saldo disponible
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findByCuentaCuentaId(1L)).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.crearMovimiento(crearMovimientoDTO);
        });

        assertEquals("Saldo no disponible", exception.getMessage());
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void crearMovimiento_CuentaNoExistente_LanzaExcepcion() {
        // Arrange
        when(cuentaRepository.findByNumeroCuenta("999999")).thenReturn(Optional.empty());
        crearMovimientoDTO.setNumeroCuenta("999999");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.crearMovimiento(crearMovimientoDTO);
        });

        assertEquals("Cuenta no encontrada", exception.getMessage());
    }
}