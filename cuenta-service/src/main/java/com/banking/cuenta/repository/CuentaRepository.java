package com.banking.cuenta.repository;

import com.banking.cuenta.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteId(Long clienteId);
    List<Cuenta> findByClienteIdAndEstadoTrue(Long clienteId);
    
    @Query("SELECT c FROM Cuenta c WHERE c.clienteId = :clienteId AND c.estado = true")
    List<Cuenta> findCuentasActivasPorCliente(@Param("clienteId") Long clienteId);
    
    boolean existsByNumeroCuenta(String numeroCuenta);
}