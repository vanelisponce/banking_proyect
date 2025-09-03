package com.banking.cliente.service;

import com.banking.cliente.dto.ClienteDTO;
import com.banking.cliente.dto.CrearClienteDTO;
import com.banking.cliente.entity.Cliente;
import com.banking.cliente.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClienteEventPublisher eventPublisher;

    public List<ClienteDTO> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
    }

    public ClienteDTO getClienteById(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(c -> modelMapper.map(c, ClienteDTO.class)).orElse(null);
    }

    public ClienteDTO getClienteByClienteId(Long clienteId) {
        Optional<Cliente> cliente = clienteRepository.findByClienteId(clienteId);
        return cliente.map(c -> modelMapper.map(c, ClienteDTO.class)).orElse(null);
    }

    public ClienteDTO crearCliente(CrearClienteDTO crearClienteDTO) {
        // GENERAR AUTOMÁTICAMENTE EL CLIENTE ID
        Long nuevoClienteId = generateNextClienteId();
        
        // VALIDAR SI YA EXISTE LA IDENTIFICACIÓN (SOLO UNA VEZ)
        if (clienteRepository.existsByIdentificacion(crearClienteDTO.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe un cliente con esa identificación");
        }

        // CREAR Y CONFIGURAR EL CLIENTE
        Cliente cliente = new Cliente();
        cliente.setNombre(crearClienteDTO.getNombre());
        cliente.setGenero(crearClienteDTO.getGenero());
        cliente.setEdad(crearClienteDTO.getEdad());
        cliente.setIdentificacion(crearClienteDTO.getIdentificacion());
        cliente.setDireccion(crearClienteDTO.getDireccion());
        cliente.setTelefono(crearClienteDTO.getTelefono());
        cliente.setClienteId(nuevoClienteId);
        cliente.setContraseña(crearClienteDTO.getContraseña());
        cliente.setEstado(true);

        // GUARDAR EL CLIENTE
        Cliente clienteGuardado = clienteRepository.save(cliente);
        
        // PUBLICAR EVENTO DE CLIENTE CREADO
        eventPublisher.publishClienteCreado(clienteGuardado);
        
        return modelMapper.map(clienteGuardado, ClienteDTO.class);
    }

    // MÉTODO PARA GENERAR EL PRÓXIMO ID
    private Long generateNextClienteId() {
        try {
            Long nextId = clienteRepository.getNextClienteId();
            return nextId != null ? nextId : 1L;
        } catch (Exception e) {
            // Si hay error con la query, usar método alternativo
            Long lastId = clienteRepository.getLastClienteId();
            return lastId != null ? lastId + 1 : 1L;
        }
    }

    public ClienteDTO actualizarCliente(Long id, CrearClienteDTO clienteDTO) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        Cliente cliente = clienteExistente.get();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setGenero(clienteDTO.getGenero());
        cliente.setEdad(clienteDTO.getEdad());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setTelefono(clienteDTO.getTelefono());
        
        if (!cliente.getContraseña().equals(clienteDTO.getContraseña())) {
            cliente.setContraseña(clienteDTO.getContraseña());
        }

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return modelMapper.map(clienteActualizado, ClienteDTO.class);
    }

    public boolean eliminarCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            // Soft delete - cambiar estado a false
            Cliente clienteEntity = cliente.get();
            clienteEntity.setEstado(false);
            clienteRepository.save(clienteEntity);
            return true;
        }
        return false;
    }
}