package com.banking.cliente.service;

import com.banking.cliente.dto.ClienteDTO;
import com.banking.cliente.dto.CrearClienteDTO;
import com.banking.cliente.entity.Cliente;
import com.banking.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ClienteEventPublisher eventPublisher;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private CrearClienteDTO crearClienteDTO;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setPersonaId(1L);
        cliente.setClienteId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("M");
        cliente.setEdad(30);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");
        cliente.setContraseña("1234");
        cliente.setEstado(true);

        crearClienteDTO = new CrearClienteDTO();
        crearClienteDTO.setNombre("Jose Lema");
        crearClienteDTO.setGenero("M");
        crearClienteDTO.setEdad(30);
        crearClienteDTO.setIdentificacion("1234567890");
        crearClienteDTO.setDireccion("Otavalo sn y principal");
        crearClienteDTO.setTelefono("098254785");
        crearClienteDTO.setClienteId(1L);
        crearClienteDTO.setContraseña("1234");

        clienteDTO = new ClienteDTO();
        clienteDTO.setClienteId(1L);
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setEstado(true);
    }

    @Test
    void crearCliente_DatosValidos_RetornaClienteDTO() {
        // Arrange
        when(clienteRepository.existsByClienteId(1L)).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);

        // Act
        ClienteDTO resultado = clienteService.crearCliente(crearClienteDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Jose Lema", resultado.getNombre());
        assertEquals(1L, resultado.getClienteId());
        assertTrue(resultado.getEstado());
        
        verify(clienteRepository, times(1)).existsByClienteId(1L);
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(eventPublisher, times(1)).publishClienteCreado(any(Cliente.class));
    }

    @Test
    void crearCliente_ClienteIdExistente_LanzaExcepcion() {
        // Arrange
        when(clienteRepository.existsByClienteId(1L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(crearClienteDTO);
        });

        assertEquals("Ya existe un cliente con ese ID", exception.getMessage());
        verify(clienteRepository, times(1)).existsByClienteId(1L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void getClienteById_ClienteExistente_RetornaClienteDTO() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);

        // Act
        ClienteDTO resultado = clienteService.getClienteById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Jose Lema", resultado.getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void getClienteById_ClienteNoExistente_RetornaNull() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ClienteDTO resultado = clienteService.getClienteById(999L);

        // Assert
        assertNull(resultado);
        verify(clienteRepository, times(1)).findById(999L);
    }
}