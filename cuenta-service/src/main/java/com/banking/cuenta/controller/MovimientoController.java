package com.banking.cuenta.controller;

import com.banking.cuenta.dto.ApiResponse;
import com.banking.cuenta.dto.CrearMovimientoDTO;
import com.banking.cuenta.dto.MovimientoDTO;
import com.banking.cuenta.service.MovimientoService;

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
@RequestMapping("/api/movimientos")
@Validated
@CrossOrigin(origins = "*")
@Tag(name = "Movimientos", description = "API para gestión de movimientos bancarios (depósitos y retiros)")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping
    @Operation(
        summary = "Crear un nuevo movimiento",
        description = "Registra un nuevo movimiento bancario (depósito o retiro) en una cuenta específica"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Movimiento creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Depósito exitoso",
                        summary = "Depósito registrado",
                        description = "Movimiento de depósito creado correctamente",
                        value = """
                        {
                            "success": true,
                            "message": "Movimiento creado exitosamente",
                            "data": {
                                "movimientoId": 1,
                                "fecha": "2024-01-15T10:30:00",
                                "tipoMovimiento": "DEPOSITO",
                                "valor": 500.0,
                                "saldo": 2500.0,
                                "numeroCuenta": "478758",
                                "clienteNombre": "Jose Lema"
                            }
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Retiro exitoso",
                        summary = "Retiro registrado",
                        description = "Movimiento de retiro creado correctamente",
                        value = """
                        {
                            "success": true,
                            "message": "Movimiento creado exitosamente",
                            "data": {
                                "movimientoId": 2,
                                "fecha": "2024-01-15T14:45:00",
                                "tipoMovimiento": "RETIRO",
                                "valor": 575.0,
                                "saldo": 1925.0,
                                "numeroCuenta": "478758",
                                "clienteNombre": "Jose Lema"
                            }
                        }
                        """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos o fondos insuficientes",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Saldo insuficiente",
                        summary = "Fondos insuficientes",
                        description = "No hay suficiente saldo para realizar el retiro",
                        value = """
                        {
                            "success": false,
                            "message": "Saldo no disponible",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Cuenta inexistente",
                        summary = "Cuenta no encontrada",
                        description = "La cuenta especificada no existe",
                        value = """
                        {
                            "success": false,
                            "message": "La cuenta con número 999999 no existe",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Valor inválido",
                        summary = "Monto inválido",
                        description = "El valor del movimiento debe ser positivo",
                        value = """
                        {
                            "success": false,
                            "message": "El valor del movimiento debe ser mayor a 0",
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
                    description = "Error interno al crear el movimiento",
                    value = """
                    {
                        "success": false,
                        "message": "Error al crear movimiento: Transaction rollback failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<MovimientoDTO>> crearMovimiento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del movimiento a crear",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CrearMovimientoDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Depósito",
                            summary = "Ejemplo de depósito",
                            description = "Movimiento de depósito en cuenta",
                            value = """
                            {
                                "numeroCuenta": "478758",
                                "tipoMovimiento": "DEPOSITO",
                                "valor": 500.0
                            }
                            """
                        ),
                        @ExampleObject(
                            name = "Retiro",
                            summary = "Ejemplo de retiro",
                            description = "Movimiento de retiro de cuenta",
                            value = """
                            {
                                "numeroCuenta": "478758",
                                "tipoMovimiento": "RETIRO",
                                "valor": 300.0
                            }
                            """
                        )
                    }
                )
            )
            @Valid @RequestBody CrearMovimientoDTO crearMovimientoDTO) {
        try {
            MovimientoDTO movimiento = movimientoService.crearMovimiento(crearMovimientoDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Movimiento creado exitosamente", movimiento));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al crear movimiento: " + e.getMessage(), null));
        }
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    @Operation(
        summary = "Obtener movimientos por cuenta",
        description = "Retorna el historial completo de movimientos de una cuenta específica"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Movimientos obtenidos exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Historial de movimientos",
                    summary = "Lista de movimientos",
                    description = "Historial completo de transacciones de la cuenta",
                    value = """
                    {
                        "success": true,
                        "message": "Movimientos obtenidos exitosamente",
                        "data": [
                            {
                                "movimientoId": 1,
                                "fecha": "2024-01-15T10:30:00",
                                "tipoMovimiento": "DEPOSITO",
                                "valor": 500.0,
                                "saldo": 2500.0,
                                "numeroCuenta": "478758",
                                "clienteNombre": "Jose Lema"
                            },
                            {
                                "movimientoId": 2,
                                "fecha": "2024-01-15T14:45:00",
                                "tipoMovimiento": "RETIRO",
                                "valor": 575.0,
                                "saldo": 1925.0,
                                "numeroCuenta": "478758",
                                "clienteNombre": "Jose Lema"
                            }
                        ]
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Número de cuenta inválido",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Cuenta inválida",
                    summary = "Número de cuenta no válido",
                    description = "El número de cuenta proporcionado no es válido",
                    value = """
                    {
                        "success": false,
                        "message": "La cuenta con número 999999 no existe o está inactiva",
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
                    description = "Error interno al obtener los movimientos",
                    value = """
                    {
                        "success": false,
                        "message": "Error al obtener movimientos: Database query timeout",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<List<MovimientoDTO>>> getMovimientosPorCuenta(
            @Parameter(description = "Número de cuenta", required = true, example = "478758")
            @PathVariable String numeroCuenta) {
        try {
            List<MovimientoDTO> movimientos = movimientoService.getMovimientosPorCuenta(numeroCuenta);
            return ResponseEntity.ok(new ApiResponse<>(true, "Movimientos obtenidos exitosamente", movimientos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener movimientos: " + e.getMessage(), null));
        }
    }
}