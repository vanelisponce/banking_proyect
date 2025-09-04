package com.banking.cliente.controller;

import com.banking.cliente.dto.ApiResponse;
import com.banking.cliente.dto.ClienteDTO;
import com.banking.cliente.dto.CrearClienteDTO;
import com.banking.cliente.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Clientes", description = "API para gestión de clientes del sistema bancario")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(
        summary = "Obtener todos los clientes",
        description = "Retorna una lista de todos los clientes registrados en el sistema"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Lista de clientes obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Lista exitosa",
                    summary = "Respuesta exitosa con lista de clientes",
                    description = "Lista de todos los clientes del sistema",
                    value = """
                    {
                        "success": true,
                        "message": "Clientes obtenidos exitosamente",
                        "data": [
                            {
                                "clienteId": 1,
                                "nombre": "Jose Lema",
                                "genero": "M",
                                "edad": 30,
                                "identificacion": "1234567890",
                                "direccion": "Otavalo sn y principal",
                                "telefono": "098254785",
                                "estado": true
                            },
                            {
                                "clienteId": 2,
                                "nombre": "Maria Rodriguez",
                                "genero": "F",
                                "edad": 25,
                                "identificacion": "0987654321",
                                "direccion": "Quito Norte",
                                "telefono": "099876543",
                                "estado": true
                            }
                        ]
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error interno",
                    summary = "Error del sistema",
                    description = "Error interno al obtener los clientes",
                    value = """
                    {
                        "success": false,
                        "message": "Error al obtener clientes: Connection timeout",
                        "data": null
                    }
                    """
                )
            )
        )
    })
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
    @Operation(
        summary = "Obtener cliente por ID",
        description = "Retorna un cliente específico basado en su ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Cliente encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cliente encontrado",
                    summary = "Respuesta exitosa",
                    description = "Cliente encontrado correctamente",
                    value = """
                    {
                        "success": true,
                        "message": "Cliente encontrado",
                        "data": {
                            "clienteId": 1,
                            "nombre": "Jose Lema",
                            "genero": "M",
                            "edad": 30,
                            "identificacion": "1234567890",
                            "direccion": "Otavalo sn y principal",
                            "telefono": "098254785",
                            "estado": true
                        }
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Cliente no encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cliente no encontrado",
                    summary = "Error 404",
                    description = "No existe un cliente con el ID especificado",
                    value = """
                    {
                        "success": false,
                        "message": "Cliente no encontrado",
                        "data": null
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error interno",
                    summary = "Error del sistema",
                    description = "Error interno al buscar el cliente",
                    value = """
                    {
                        "success": false,
                        "message": "Error al obtener cliente: Database connection failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<ClienteDTO>> getClienteById(
            @Parameter(description = "ID único del cliente", required = true, example = "1")
            @PathVariable Long id) {
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
    @Operation(
        summary = "Crear un nuevo cliente",
        description = "Registra un nuevo cliente en el sistema bancario"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Cliente creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cliente creado",
                    summary = "Creación exitosa",
                    description = "Cliente registrado correctamente en el sistema",
                    value = """
                    {
                        "success": true,
                        "message": "Cliente creado exitosamente",
                        "data": {
                            "clienteId": 5,
                            "nombre": "Ana Garcia",
                            "genero": "F",
                            "edad": 28,
                            "identificacion": "1122334455",
                            "direccion": "Cuenca Centro",
                            "telefono": "097123456",
                            "estado": true
                        }
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos o cliente ya existe",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Datos inválidos",
                        summary = "Error de validación",
                        description = "Los datos enviados no cumplen las validaciones requeridas",
                        value = """
                        {
                            "success": false,
                            "message": "La edad debe ser mayor a 18 años",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Cliente duplicado",
                        summary = "Cliente ya existe",
                        description = "Ya existe un cliente registrado con esa identificación",
                        value = """
                        {
                            "success": false,
                            "message": "Ya existe un cliente con la identificación 1234567890",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Campos requeridos",
                        summary = "Campos obligatorios faltantes",
                        description = "Faltan campos obligatorios en la solicitud",
                        value = """
                        {
                            "success": false,
                            "message": "El campo 'nombre' es obligatorio",
                            "data": null
                        }
                        """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error interno",
                    summary = "Error del sistema",
                    description = "Error interno al crear el cliente",
                    value = """
                    {
                        "success": false,
                        "message": "Error al crear cliente: Database insert failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<ClienteDTO>> crearCliente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del cliente a crear",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CrearClienteDTO.class),
                    examples = @ExampleObject(
                        name = "Ejemplo Cliente",
                        summary = "Ejemplo de creación de cliente",
                        description = "Datos completos para crear un nuevo cliente",
                        value = """
                        {
                          "nombre": "Ana Garcia",
                          "genero": "F",
                          "edad": 28,
                          "identificacion": "1122334455",
                          "direccion": "Cuenca Centro",
                          "telefono": "097123456",
                          "contraseña": "securePass123"
                        }
                        """
                    )
                )
            )
            @Valid @RequestBody CrearClienteDTO crearClienteDTO) {
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
    @Operation(
        summary = "Actualizar cliente",
        description = "Actualiza la información de un cliente existente"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Cliente actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Actualización exitosa",
                    summary = "Cliente actualizado",
                    description = "Cliente actualizado correctamente en el sistema",
                    value = """
                    {
                        "success": true,
                        "message": "Cliente actualizado exitosamente",
                        "data": {
                            "clienteId": 1,
                            "nombre": "Jose Lema Actualizado",
                            "genero": "M",
                            "edad": 31,
                            "identificacion": "1234567890",
                            "direccion": "Otavalo Norte, Calle Principal",
                            "telefono": "098254786",
                            "estado": true
                        }
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Validación fallida",
                        summary = "Error de validación",
                        description = "Los datos de actualización no son válidos",
                        value = """
                        {
                            "success": false,
                            "message": "El número de teléfono debe tener entre 9 y 15 dígitos",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Identificación duplicada",
                        summary = "Identificación ya existe",
                        description = "La identificación ya está registrada por otro cliente",
                        value = """
                        {
                            "success": false,
                            "message": "La identificación 0987654321 ya está registrada",
                            "data": null
                        }
                        """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Cliente no encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cliente inexistente",
                    summary = "ID no válido",
                    description = "No se encontró un cliente con el ID especificado",
                    value = """
                    {
                        "success": false,
                        "message": "No se encontró un cliente con ID 999",
                        "data": null
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error interno",
                    summary = "Error del sistema",
                    description = "Error interno al actualizar el cliente",
                    value = """
                    {
                        "success": false,
                        "message": "Error al actualizar cliente: Database update failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<ClienteDTO>> actualizarCliente(
            @Parameter(description = "ID del cliente a actualizar", required = true, example = "1")
            @PathVariable Long id,
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
    @Operation(
        summary = "Eliminar cliente",
        description = "Desactiva un cliente del sistema (soft delete)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Cliente eliminado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Eliminación exitosa",
                    summary = "Cliente desactivado",
                    description = "Cliente desactivado correctamente del sistema",
                    value = """
                    {
                        "success": true,
                        "message": "Cliente eliminado exitosamente",
                        "data": "Cliente desactivado"
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Cliente no encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cliente no existe",
                    summary = "ID inválido",
                    description = "No se encontró un cliente activo con el ID especificado",
                    value = """
                    {
                        "success": false,
                        "message": "Cliente no encontrado",
                        "data": null
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error interno",
                    summary = "Error del sistema",
                    description = "Error interno al eliminar el cliente",
                    value = """
                    {
                        "success": false,
                        "message": "Error al eliminar cliente: Database delete failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<String>> eliminarCliente(
            @Parameter(description = "ID del cliente a eliminar", required = true, example = "1")
            @PathVariable Long id) {
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