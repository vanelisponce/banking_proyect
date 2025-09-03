package com.banking.cliente.repository;

import com.banking.cliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByClienteId(Long clienteId);
    Optional<Cliente> findByIdentificacion(String identificacion);
    List<Cliente> findByEstadoTrue();
    
    @Query("SELECT c FROM Cliente c WHERE c.nombre LIKE %:nombre%")
    List<Cliente> findByNombreContaining(@Param("nombre") String nombre);
    
    boolean existsByClienteId(Long clienteId);
    boolean existsByIdentificacion(String identificacion);
}