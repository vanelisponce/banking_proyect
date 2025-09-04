package com.banking.cuenta.controller;

import com.banking.cuenta.dto.ApiResponse;
import com.banking.cuenta.dto.CrearCuentaDTO;
import com.banking.cuenta.dto.CuentaDTO;
import com.banking.cuenta.service.CuentaService;

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
@RequestMapping("/api/cuentas")
@Validated
@CrossOrigin(origins = "*")
@Tag(name = "Cuentas", description = "API para gestión de cuentas bancarias")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    @Operation(
        summary = "Obtener todas las cuentas",
        description = "Retorna una lista de todas las cuentas bancarias registradas en el sistema"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Lista de cuentas obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Lista exitosa",
                    summary = "Respuesta exitosa con lista de cuentas",
                    description = "Lista de todas las cuentas del sistema",
                    value = """
                    {
                        "success": true,
                        "message": "Cuentas obtenidas exitosamente",
                        "data": [
                            {
                                "cuentaId": 1,
                                "numeroCuenta": "478758",
                                "tipoCuenta": "AHORRO",
                                "saldoInicial": 2000.0,
                                "estado": true,
                                "clienteId": 1,
                                "clienteNombre": "Jose Lema"
                            },
                            {
                                "cuentaId": 2,
                                "numeroCuenta": "225487",
                                "tipoCuenta": "CORRIENTE",
                                "saldoInicial": 100.0,
                                "estado": true,
                                "clienteId": 2,
                                "clienteNombre": "Maria Rodriguez"
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
                    description = "Error interno al obtener las cuentas",
                    value = """
                    {
                        "success": false,
                        "message": "Error al obtener cuentas: Database connection timeout",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<List<CuentaDTO>>> getAllCuentas() {
        try {
            List<CuentaDTO> cuentas = cuentaService.getAllCuentas();
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuentas obtenidas exitosamente", cuentas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener cuentas: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener cuenta por ID",
        description = "Retorna una cuenta específica basada en su ID único"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Cuenta encontrada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cuenta encontrada",
                    summary = "Respuesta exitosa",
                    description = "Cuenta encontrada correctamente",
                    value = """
                    {
                        "success": true,
                        "message": "Cuenta encontrada",
                        "data": {
                            "cuentaId": 1,
                            "numeroCuenta": "478758",
                            "tipoCuenta": "AHORRO",
                            "saldoInicial": 2000.0,
                            "estado": true,
                            "clienteId": 1,
                            "clienteNombre": "Jose Lema"
                        }
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Cuenta no encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cuenta no encontrada",
                    summary = "Error 404",
                    description = "No existe una cuenta con el ID especificado",
                    value = """
                    {
                        "success": false,
                        "message": "Cuenta no encontrada",
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
                    description = "Error interno al buscar la cuenta",
                    value = """
                    {
                        "success": false,
                        "message": "Error al obtener cuenta: Database query failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<CuentaDTO>> getCuentaById(
            @Parameter(description = "ID único de la cuenta", required = true, example = "1")
            @PathVariable Long id) {
        try {
            CuentaDTO cuenta = cuentaService.getCuentaById(id);
            if (cuenta != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta encontrada", cuenta));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Cuenta no encontrada", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener cuenta: " + e.getMessage(), null));
        }
    }

    @GetMapping("/numero/{numeroCuenta}")
    @Operation(
        summary = "Obtener cuenta por número",
        description = "Retorna una cuenta específica basada en su número de cuenta"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Cuenta encontrada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cuenta encontrada por número",
                    summary = "Búsqueda exitosa por número",
                    description = "Cuenta encontrada usando número de cuenta",
                    value = """
                    {
                        "success": true,
                        "message": "Cuenta encontrada",
                        "data": {
                            "cuentaId": 1,
                            "numeroCuenta": "478758",
                            "tipoCuenta": "AHORRO",
                            "saldoInicial": 2000.0,
                            "estado": true,
                            "clienteId": 1,
                            "clienteNombre": "Jose Lema"
                        }
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Cuenta no encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Número de cuenta inválido",
                    summary = "Cuenta inexistente",
                    description = "No existe una cuenta con el número especificado",
                    value = """
                    {
                        "success": false,
                        "message": "Cuenta no encontrada",
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
                    description = "Error interno al buscar por número de cuenta",
                    value = """
                    {
                        "success": false,
                        "message": "Error al obtener cuenta: Database connection failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<CuentaDTO>> getCuentaByNumero(
            @Parameter(description = "Número de cuenta", required = true, example = "478758")
            @PathVariable String numeroCuenta) {
        try {
            CuentaDTO cuenta = cuentaService.getCuentaByNumero(numeroCuenta);
            if (cuenta != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta encontrada", cuenta));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Cuenta no encontrada", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener cuenta: " + e.getMessage(), null));
        }
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(
        summary = "Obtener cuentas por cliente",
        description = "Retorna todas las cuentas asociadas a un cliente específico"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Cuentas del cliente obtenidas exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cuentas del cliente",
                    summary = "Lista de cuentas por cliente",
                    description = "Todas las cuentas asociadas al cliente",
                    value = """
                    {
                        "success": true,
                        "message": "Cuentas del cliente obtenidas",
                        "data": [
                            {
                                "cuentaId": 1,
                                "numeroCuenta": "478758",
                                "tipoCuenta": "AHORRO",
                                "saldoInicial": 2000.0,
                                "estado": true,
                                "clienteId": 1,
                                "clienteNombre": "Jose Lema"
                            },
                            {
                                "cuentaId": 3,
                                "numeroCuenta": "985746",
                                "tipoCuenta": "CORRIENTE",
                                "saldoInicial": 1000.0,
                                "estado": true,
                                "clienteId": 1,
                                "clienteNombre": "Jose Lema"
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
                    description = "Error interno al obtener cuentas del cliente",
                    value = """
                    {
                        "success": false,
                        "message": "Error al obtener cuentas: Client validation failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<List<CuentaDTO>>> getCuentasByClienteId(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable Long clienteId) {
        try {
            List<CuentaDTO> cuentas = cuentaService.getCuentasByClienteId(clienteId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuentas del cliente obtenidas", cuentas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener cuentas: " + e.getMessage(), null));
        }
    }

    @PostMapping
    @Operation(
        summary = "Crear una nueva cuenta",
        description = "Registra una nueva cuenta bancaria en el sistema"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Cuenta creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cuenta creada",
                    summary = "Creación exitosa",
                    description = "Cuenta bancaria registrada correctamente",
                    value = """
                    {
                        "success": true,
                        "message": "Cuenta creada exitosamente",
                        "data": {
                            "cuentaId": 4,
                            "numeroCuenta": "123456",
                            "tipoCuenta": "AHORRO",
                            "saldoInicial": 1500.0,
                            "estado": true,
                            "clienteId": 2,
                            "clienteNombre": "Maria Rodriguez"
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
                        name = "Cliente inexistente",
                        summary = "Cliente no encontrado",
                        description = "El cliente especificado no existe",
                        value = """
                        {
                            "success": false,
                            "message": "El cliente con ID 999 no existe",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Número duplicado",
                        summary = "Cuenta ya existe",
                        description = "Ya existe una cuenta con ese número",
                        value = """
                        {
                            "success": false,
                            "message": "Ya existe una cuenta con el número 478758",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Saldo inválido",
                        summary = "Saldo inicial inválido",
                        description = "El saldo inicial no puede ser negativo",
                        value = """
                        {
                            "success": false,
                            "message": "El saldo inicial no puede ser negativo",
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
                    description = "Error interno al crear la cuenta",
                    value = """
                    {
                        "success": false,
                        "message": "Error al crear cuenta: Database insert failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<CuentaDTO>> crearCuenta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de la cuenta a crear",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CrearCuentaDTO.class),
                    examples = @ExampleObject(
                        name = "Ejemplo Cuenta",
                        summary = "Ejemplo de creación de cuenta",
                        description = "Datos para crear una nueva cuenta bancaria",
                        value = """
                        {
                            "numeroCuenta": "123456",
                            "tipoCuenta": "AHORRO",
                            "saldoInicial": 1500.0,
                            "clienteId": 2
                        }
                        """
                    )
                )
            )
            @Valid @RequestBody CrearCuentaDTO crearCuentaDTO) {
        try {
            CuentaDTO cuentaCreada = cuentaService.crearCuenta(crearCuentaDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Cuenta creada exitosamente", cuentaCreada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al crear cuenta: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar cuenta",
        description = "Actualiza la información de una cuenta bancaria existente"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Cuenta actualizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Actualización exitosa",
                    summary = "Cuenta actualizada",
                    description = "Cuenta actualizada correctamente en el sistema",
                    value = """
                    {
                        "success": true,
                        "message": "Cuenta actualizada exitosamente",
                        "data": {
                            "cuentaId": 1,
                            "numeroCuenta": "478758",
                            "tipoCuenta": "CORRIENTE",
                            "saldoInicial": 2500.0,
                            "estado": true,
                            "clienteId": 1,
                            "clienteNombre": "Jose Lema"
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
                            "message": "El tipo de cuenta debe ser AHORRO o CORRIENTE",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Cliente inválido",
                        summary = "Cliente no encontrado",
                        description = "El cliente especificado no existe",
                        value = """
                        {
                            "success": false,
                            "message": "El cliente con ID 999 no existe",
                            "data": null
                        }
                        """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Cuenta no encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cuenta inexistente",
                    summary = "ID no válido",
                    description = "No se encontró una cuenta con el ID especificado",
                    value = """
                    {
                        "success": false,
                        "message": "No se encontró una cuenta con ID 999",
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
                    description = "Error interno al actualizar la cuenta",
                    value = """
                    {
                        "success": false,
                        "message": "Error al actualizar cuenta: Database update failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<CuentaDTO>> actualizarCuenta(
            @Parameter(description = "ID de la cuenta a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CrearCuentaDTO cuentaDTO) {
        try {
            CuentaDTO cuentaActualizada = cuentaService.actualizarCuenta(id, cuentaDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta actualizada exitosamente", cuentaActualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al actualizar cuenta: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar cuenta",
        description = "Desactiva una cuenta bancaria del sistema (soft delete)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Cuenta eliminada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Eliminación exitosa",
                    summary = "Cuenta desactivada",
                    description = "Cuenta desactivada correctamente del sistema",
                    value = """
                    {
                        "success": true,
                        "message": "Cuenta eliminada exitosamente",
                        "data": "Cuenta desactivada"
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Cuenta no encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cuenta no existe",
                    summary = "ID inválido",
                    description = "No se encontró una cuenta activa con el ID especificado",
                    value = """
                    {
                        "success": false,
                        "message": "Cuenta no encontrada",
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
                    description = "Error interno al eliminar la cuenta",
                    value = """
                    {
                        "success": false,
                        "message": "Error al eliminar cuenta: Database delete failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<String>> eliminarCuenta(
            @Parameter(description = "ID de la cuenta a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        try {
            boolean eliminado = cuentaService.eliminarCuenta(id);
            if (eliminado) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta eliminada exitosamente", "Cuenta desactivada"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Cuenta no encontrada", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al eliminar cuenta: " + e.getMessage(), null));
        }
    }
}