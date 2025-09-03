package com.banking.cuenta.service;

import com.banking.cuenta.dto.CrearMovimientoDTO;
import com.banking.cuenta.dto.MovimientoDTO;
import com.banking.cuenta.entity.Cuenta;
import com.banking.cuenta.entity.Movimiento;
import com.banking.cuenta.repository.CuentaRepository;
import com.banking.cuenta.repository.MovimientoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MovimientoDTO crearMovimiento(CrearMovimientoDTO crearMovimientoDTO) {
        // Buscar la cuenta
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByNumeroCuenta(crearMovimientoDTO.getNumeroCuenta());
        if (cuentaOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        Cuenta cuenta = cuentaOpt.get();
        if (!cuenta.getEstado()) {
            throw new IllegalArgumentException("La cuenta no está activa");
        }

        // Calcular saldo actual
        BigDecimal saldoActual = calcularSaldoActual(cuenta);
        BigDecimal valorMovimiento = crearMovimientoDTO.getValor();
        BigDecimal nuevoSaldo = saldoActual.add(valorMovimiento);

        // Validar saldo suficiente para retiros
        if (valorMovimiento.compareTo(BigDecimal.ZERO) < 0 && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo no disponible");
        }

        // Crear el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(new Date());
        movimiento.setTipoMovimiento(valorMovimiento.compareTo(BigDecimal.ZERO) > 0 ? "Depósito" : "Retiro");
        movimiento.setValor(valorMovimiento);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
        
        MovimientoDTO dto = modelMapper.map(movimientoGuardado, MovimientoDTO.class);
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        return dto;
    }

    public List<MovimientoDTO> getMovimientosPorCuenta(String numeroCuenta) {
        Optional<Cuenta> cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta);
        if (cuenta.isEmpty()) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdOrderByFechaDesc(cuenta.get().getCuentaId());
        return movimientos.stream()
                .map(mov -> {
                    MovimientoDTO dto = modelMapper.map(mov, MovimientoDTO.class);
                    dto.setNumeroCuenta(numeroCuenta);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calcularSaldoActual(Cuenta cuenta) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaCuentaId(cuenta.getCuentaId());
        BigDecimal saldoMovimientos = movimientos.stream()
                .map(Movimiento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return cuenta.getSaldoInicial().add(saldoMovimientos);
    }
}