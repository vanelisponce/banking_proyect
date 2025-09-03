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

import java.util.Arrays;
import java.util.List;
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
        // Configurar cliente de prueba
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

        // Configurar DTO de creación (SIN clienteId)
        crearClienteDTO = new CrearClienteDTO();
        crearClienteDTO.setNombre("Jose Lema");
        crearClienteDTO.setGenero("M");
        crearClienteDTO.setEdad(30);
        crearClienteDTO.setIdentificacion("1234567890");
        crearClienteDTO.setDireccion("Otavalo sn y principal");
        crearClienteDTO.setTelefono("098254785");
        crearClienteDTO.setContraseña("1234");

        // Configurar DTO de respuesta
        clienteDTO = new ClienteDTO();
        clienteDTO.setClienteId(1L);
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setGenero("M");
        clienteDTO.setEdad(30);
        clienteDTO.setIdentificacion("1234567890");
        clienteDTO.setDireccion("Otavalo sn y principal");
        clienteDTO.setTelefono("098254785");
        clienteDTO.setEstado(true);
    }

    @Test
    void crearCliente_DatosValidos_RetornaClienteDTO() {
        // Arrange
        when(clienteRepository.getNextClienteId()).thenReturn(1L);
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
        assertEquals("M", resultado.getGenero());
        assertEquals(30, resultado.getEdad());
        assertEquals("1234567890", resultado.getIdentificacion());
        
        verify(clienteRepository, times(1)).getNextClienteId();
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(eventPublisher, times(1)).publishClienteCreado(any(Cliente.class));
    }

    @Test
    void crearCliente_IdentificacionExistente_LanzaExcepcion() {
        // Arrange
        when(clienteRepository.getNextClienteId()).thenReturn(2L);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(crearClienteDTO);
        });

        assertEquals("Ya existe un cliente con esa identificación", exception.getMessage());
        verify(clienteRepository, times(1)).getNextClienteId();
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, never()).save(any(Cliente.class));
        verify(eventPublisher, never()).publishClienteCreado(any(Cliente.class));
    }

    @Test
    void crearCliente_ErrorGenerandoId_UsaMetodoAlternativo() {
        // Arrange
        when(clienteRepository.getNextClienteId()).thenThrow(new RuntimeException("Error en query"));
        when(clienteRepository.getLastClienteId()).thenReturn(5L);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        
        Cliente clienteConIdGenerado = new Cliente();
        clienteConIdGenerado.setClienteId(6L);
        clienteConIdGenerado.setNombre("Jose Lema");
        
        ClienteDTO dtoConIdGenerado = new ClienteDTO();
        dtoConIdGenerado.setClienteId(6L);
        dtoConIdGenerado.setNombre("Jose Lema");
        
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteConIdGenerado);
        when(modelMapper.map(clienteConIdGenerado, ClienteDTO.class)).thenReturn(dtoConIdGenerado);

        // Act
        ClienteDTO resultado = clienteService.crearCliente(crearClienteDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(6L, resultado.getClienteId());
        verify(clienteRepository, times(1)).getNextClienteId();
        verify(clienteRepository, times(1)).getLastClienteId();
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void crearCliente_PrimerClienteDelSistema_AsignaId1() {
        // Arrange - Simular base de datos vacía
        when(clienteRepository.getNextClienteId()).thenReturn(1L);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);

        // Act
        ClienteDTO resultado = clienteService.crearCliente(crearClienteDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getClienteId());
        verify(clienteRepository, times(1)).getNextClienteId();
    }

    @Test
    void getAllClientes_ListaVacia_RetornaListaVacia() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<ClienteDTO> resultado = clienteService.getAllClientes();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void getAllClientes_ClientesExistentes_RetornaListaClientes() {
        // Arrange
        Cliente cliente2 = new Cliente();
        cliente2.setClienteId(2L);
        cliente2.setNombre("Maria Garcia");
        
        ClienteDTO clienteDTO2 = new ClienteDTO();
        clienteDTO2.setClienteId(2L);
        clienteDTO2.setNombre("Maria Garcia");

        List<Cliente> clientes = Arrays.asList(cliente, cliente2);
        when(clienteRepository.findAll()).thenReturn(clientes);
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);
        when(modelMapper.map(cliente2, ClienteDTO.class)).thenReturn(clienteDTO2);

        // Act
        List<ClienteDTO> resultado = clienteService.getAllClientes();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Jose Lema", resultado.get(0).getNombre());
        assertEquals("Maria Garcia", resultado.get(1).getNombre());
        verify(clienteRepository, times(1)).findAll();
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
        assertEquals(1L, resultado.getClienteId());
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

    @Test
    void getClienteByClienteId_ClienteExistente_RetornaClienteDTO() {
        // Arrange
        when(clienteRepository.findByClienteId(1L)).thenReturn(Optional.of(cliente));
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);

        // Act
        ClienteDTO resultado = clienteService.getClienteByClienteId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Jose Lema", resultado.getNombre());
        assertEquals(1L, resultado.getClienteId());
        verify(clienteRepository, times(1)).findByClienteId(1L);
    }

    @Test
    void getClienteByClienteId_ClienteNoExistente_RetornaNull() {
        // Arrange
        when(clienteRepository.findByClienteId(999L)).thenReturn(Optional.empty());

        // Act
        ClienteDTO resultado = clienteService.getClienteByClienteId(999L);

        // Assert
        assertNull(resultado);
        verify(clienteRepository, times(1)).findByClienteId(999L);
    }

    @Test
    void actualizarCliente_ClienteExistente_RetornaClienteActualizado() {
        // Arrange
        CrearClienteDTO clienteActualizacion = new CrearClienteDTO();
        clienteActualizacion.setNombre("Jose Lema Actualizado");
        clienteActualizacion.setGenero("M");
        clienteActualizacion.setEdad(31);
        clienteActualizacion.setIdentificacion("1234567890");
        clienteActualizacion.setDireccion("Nueva Direccion");
        clienteActualizacion.setTelefono("099999999");
        clienteActualizacion.setContraseña("nueva123");

        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setClienteId(1L);
        clienteActualizado.setNombre("Jose Lema Actualizado");
        clienteActualizado.setEdad(31);
        clienteActualizado.setDireccion("Nueva Direccion");

        ClienteDTO clienteDTOActualizado = new ClienteDTO();
        clienteDTOActualizado.setClienteId(1L);
        clienteDTOActualizado.setNombre("Jose Lema Actualizado");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);
        when(modelMapper.map(clienteActualizado, ClienteDTO.class)).thenReturn(clienteDTOActualizado);

        // Act
        ClienteDTO resultado = clienteService.actualizarCliente(1L, clienteActualizacion);

        // Assert
        assertNotNull(resultado);
        assertEquals("Jose Lema Actualizado", resultado.getNombre());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void actualizarCliente_ClienteNoExistente_LanzaExcepcion() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.actualizarCliente(999L, crearClienteDTO);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void eliminarCliente_ClienteExistente_RealizaSoftDelete() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        boolean resultado = clienteService.eliminarCliente(1L);

        // Assert
        assertTrue(resultado);
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        
        // Verificar que se llamó save con el cliente con estado false
        verify(clienteRepository).save(argThat(c -> !c.getEstado()));
    }

    @Test
    void eliminarCliente_ClienteNoExistente_RetornaFalse() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        boolean resultado = clienteService.eliminarCliente(999L);

        // Assert
        assertFalse(resultado);
        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void crearCliente_DatosCompletos_GuardaTodosLosCampos() {
        // Arrange
        CrearClienteDTO clienteCompleto = new CrearClienteDTO();
        clienteCompleto.setNombre("Maria Garcia");
        clienteCompleto.setGenero("F");
        clienteCompleto.setEdad(25);
        clienteCompleto.setIdentificacion("0987654321");
        clienteCompleto.setDireccion("Av. Amazonas 123");
        clienteCompleto.setTelefono("098765432");
        clienteCompleto.setContraseña("abc123");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setClienteId(3L);
        clienteGuardado.setNombre("Maria Garcia");

        ClienteDTO respuestaDTO = new ClienteDTO();
        respuestaDTO.setClienteId(3L);
        respuestaDTO.setNombre("Maria Garcia");

        when(clienteRepository.getNextClienteId()).thenReturn(3L);
        when(clienteRepository.existsByIdentificacion("0987654321")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(modelMapper.map(clienteGuardado, ClienteDTO.class)).thenReturn(respuestaDTO);

        // Act
        ClienteDTO resultado = clienteService.crearCliente(clienteCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getClienteId());
        assertEquals("Maria Garcia", resultado.getNombre());

        // Verificar que existsByIdentificacion se llama exactamente 1 vez
        verify(clienteRepository, times(1)).existsByIdentificacion("0987654321");

        // Verificar que save fue llamado con todos los campos correctos
        verify(clienteRepository).save(argThat(c -> 
            c.getNombre().equals("Maria Garcia") &&
            c.getGenero().equals("F") &&
            c.getEdad().equals(25) &&
            c.getIdentificacion().equals("0987654321") &&
            c.getDireccion().equals("Av. Amazonas 123") &&
            c.getTelefono().equals("098765432") &&
            c.getContraseña().equals("abc123") &&
            c.getEstado().equals(true) &&
            c.getClienteId().equals(3L)
        ));
    }

    // Nuevo test para verificar el flujo completo sin duplicaciones
    @Test 
    void crearCliente_VerificarLlamadasUnicas() {
        // Arrange
        when(clienteRepository.getNextClienteId()).thenReturn(1L);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);

        // Act
        clienteService.crearCliente(crearClienteDTO);

        // Assert - Verificar que cada método se llama exactamente las veces esperadas
        verify(clienteRepository, times(1)).getNextClienteId();
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(eventPublisher, times(1)).publishClienteCreado(any(Cliente.class));
        verify(modelMapper, times(1)).map(any(Cliente.class), eq(ClienteDTO.class));
    }
}