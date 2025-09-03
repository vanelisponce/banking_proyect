package com.banking.cuenta.repository;

import com.banking.cuenta.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaCuentaId(Long cuentaId);
    
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.cuentaId = :cuentaId ORDER BY m.fecha DESC")
    List<Movimiento> findByCuentaIdOrderByFechaDesc(@Param("cuentaId") Long cuentaId);
    
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.cuentaId = :cuentaId " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha")
    List<Movimiento> findByCuentaIdAndFechaBetween(
            @Param("cuentaId") Long cuentaId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
    
    @Query("SELECT m FROM Movimiento m JOIN m.cuenta c WHERE c.clienteId = :clienteId " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha")
    List<Movimiento> findByClienteIdAndFechaBetween(
            @Param("clienteId") Long clienteId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
}