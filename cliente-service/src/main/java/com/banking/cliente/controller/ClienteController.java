package com.banking.cliente.controller;

import com.banking.cliente.dto.ApiResponse;
import com.banking.cliente.dto.ClienteDTO;
import com.banking.cliente.dto.CrearClienteDTO;
import com.banking.cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Validated
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> getAllClientes() {
        try {
            List<ClienteDTO> clientes = clienteService.getAllClientes();
            return ResponseEntity.ok(new ApiResponse<>(true, "Clientes obtenidos exitosamente", clientes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener clientes: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> getClienteById(@PathVariable Long id) {
        try {
            ClienteDTO cliente = clienteService.getClienteById(id);
            if (cliente != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Cliente encontrado", cliente));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Cliente no encontrado", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener cliente: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteDTO>> crearCliente(@Valid @RequestBody CrearClienteDTO crearClienteDTO) {
        try {
            ClienteDTO clienteCreado = clienteService.crearCliente(crearClienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Cliente creado exitosamente", clienteCreado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al crear cliente: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> actualizarCliente(@PathVariable Long id,
                                                                    @Valid @RequestBody CrearClienteDTO clienteDTO) {
        try {
            ClienteDTO clienteActualizado = clienteService.actualizarCliente(id, clienteDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cliente actualizado exitosamente", clienteActualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al actualizar cliente: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarCliente(@PathVariable Long id) {
        try {
            boolean eliminado = clienteService.eliminarCliente(id);
            if (eliminado) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Cliente eliminado exitosamente", "Cliente desactivado"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Cliente no encontrado", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al eliminar cliente: " + e.getMessage(), null));
        }
    }
}