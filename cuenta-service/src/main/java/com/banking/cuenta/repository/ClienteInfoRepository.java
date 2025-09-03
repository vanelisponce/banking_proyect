package com.banking.cuenta.repository;

import com.banking.cuenta.entity.ClienteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteInfoRepository extends JpaRepository<ClienteInfo, Long> {
    Optional<ClienteInfo> findByClienteId(Long clienteId);
    Optional<ClienteInfo> findByIdentificacion(String identificacion);
}