package com.banking.cuenta.service;

import com.banking.cuenta.dto.ClienteInfoDTO;
import com.banking.cuenta.dto.ReporteEstadoCuentaDTO;
import com.banking.cuenta.entity.Cuenta;
import com.banking.cuenta.entity.Movimiento;
import com.banking.cuenta.repository.CuentaRepository;
import com.banking.cuenta.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ClienteInfoService clienteInfoService;

    public ReporteEstadoCuentaDTO generarReporteEstadoCuenta(Long clienteId, Date fechaInicio, Date fechaFin) {
        // Obtener información del cliente
        ClienteInfoDTO clienteInfo = clienteInfoService.getClienteInfo(clienteId);
        if (clienteInfo == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        // Obtener cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findByClienteIdAndEstadoTrue(clienteId);

        ReporteEstadoCuentaDTO reporte = new ReporteEstadoCuentaDTO();
        reporte.setCliente(clienteInfo.getNombre());
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);

        List<ReporteEstadoCuentaDTO.CuentaReporteDTO> cuentasReporte = cuentas.stream().map(cuenta -> {
            ReporteEstadoCuentaDTO.CuentaReporteDTO cuentaReporte = new ReporteEstadoCuentaDTO.CuentaReporteDTO();
            cuentaReporte.setNumeroCuenta(cuenta.getNumeroCuenta());
            cuentaReporte.setTipo(cuenta.getTipoCuenta());
            cuentaReporte.setSaldoInicial(cuenta.getSaldoInicial());

            // Obtener movimientos en el rango de fechas
            List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                    cuenta.getCuentaId(), fechaInicio, fechaFin);

            // Calcular saldo disponible
            BigDecimal saldoDisponible = calcularSaldoDisponible(cuenta);
            cuentaReporte.setSaldoDisponible(saldoDisponible);

            // Mapear movimientos
            List<ReporteEstadoCuentaDTO.MovimientoReporteDTO> movimientosReporte = movimientos.stream().map(mov -> {
                ReporteEstadoCuentaDTO.MovimientoReporteDTO movReporte = new ReporteEstadoCuentaDTO.MovimientoReporteDTO();
                movReporte.setFecha(mov.getFecha());
                movReporte.setTipoMovimiento(mov.getTipoMovimiento());
                movReporte.setValor(mov.getValor());
                movReporte.setSaldo(mov.getSaldo());
                return movReporte;
            }).collect(Collectors.toList());

            cuentaReporte.setMovimientos(movimientosReporte);
            return cuentaReporte;
        }).collect(Collectors.toList());

        reporte.setCuentas(cuentasReporte);
        return reporte;
    }

    private BigDecimal calcularSaldoDisponible(Cuenta cuenta) {
        List<Movimiento> todosLosMovimientos = movimientoRepository.findByCuentaCuentaId(cuenta.getCuentaId());
        BigDecimal sumaMovimientos = todosLosMovimientos.stream()
                .map(Movimiento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return cuenta.getSaldoInicial().add(sumaMovimientos);
    }
}