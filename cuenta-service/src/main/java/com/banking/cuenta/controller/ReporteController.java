package com.banking.cuenta.controller;

import com.banking.cuenta.dto.ApiResponse;
import com.banking.cuenta.dto.ReporteEstadoCuentaDTO;
import com.banking.cuenta.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
@Tag(name = "Reportes", description = "API para generación de reportes y estados de cuenta")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    @Operation(
        summary = "Generar reporte de estado de cuenta",
        description = "Genera un reporte detallado del estado de cuenta de un cliente en un rango de fechas específico, incluyendo información del cliente, sus cuentas y movimientos realizados"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Reporte generado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Reporte completo",
                    summary = "Estado de cuenta generado",
                    description = "Reporte detallado con información del cliente y movimientos",
                    value = """
                    {
                        "success": true,
                        "message": "Reporte generado exitosamente",
                        "data": {
                            "clienteId": 1,
                            "clienteNombre": "Jose Lema",
                            "fechaInicio": "2024-01-01",
                            "fechaFin": "2024-01-31",
                            "cuentas": [
                                {
                                    "numeroCuenta": "478758",
                                    "tipoCuenta": "AHORRO",
                                    "saldoInicial": 2000.0,
                                    "saldoActual": 2925.0,
                                    "totalDepositos": 1500.0,
                                    "totalRetiros": 575.0,
                                    "cantidadMovimientos": 3,
                                    "movimientos": [
                                        {
                                            "movimientoId": 1,
                                            "fecha": "2024-01-15T10:30:00",
                                            "tipoMovimiento": "DEPOSITO",
                                            "valor": 500.0,
                                            "saldo": 2500.0
                                        },
                                        {
                                            "movimientoId": 2,
                                            "fecha": "2024-01-15T14:45:00",
                                            "tipoMovimiento": "RETIRO",
                                            "valor": 575.0,
                                            "saldo": 1925.0
                                        },
                                        {
                                            "movimientoId": 3,
                                            "fecha": "2024-01-16T09:15:00",
                                            "tipoMovimiento": "DEPOSITO",
                                            "valor": 1000.0,
                                            "saldo": 2925.0
                                        }
                                    ]
                                },
                                {
                                    "numeroCuenta": "225487",
                                    "tipoCuenta": "CORRIENTE",
                                    "saldoInicial": 100.0,
                                    "saldoActual": 600.0,
                                    "totalDepositos": 500.0,
                                    "totalRetiros": 0.0,
                                    "cantidadMovimientos": 1,
                                    "movimientos": [
                                        {
                                            "movimientoId": 4,
                                            "fecha": "2024-01-20T16:30:00",
                                            "tipoMovimiento": "DEPOSITO",
                                            "valor": 500.0,
                                            "saldo": 600.0
                                        }
                                    ]
                                }
                            ],
                            "resumenGeneral": {
                                "totalCuentas": 2,
                                "saldoTotalInicial": 2100.0,
                                "saldoTotalActual": 3525.0,
                                "totalDepositosGeneral": 2000.0,
                                "totalRetirosGeneral": 575.0,
                                "totalMovimientos": 4
                            }
                        }
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Parámetros inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Cliente inexistente",
                        summary = "Cliente no encontrado",
                        description = "El cliente especificado no existe en el sistema",
                        value = """
                        {
                            "success": false,
                            "message": "El cliente con ID 999 no existe",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Rango de fechas inválido",
                        summary = "Fechas incorrectas",
                        description = "El rango de fechas proporcionado no es válido",
                        value = """
                        {
                            "success": false,
                            "message": "La fecha de inicio debe ser anterior a la fecha fin",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Formato de fecha inválido",
                        summary = "Formato incorrecto",
                        description = "Las fechas deben estar en formato yyyy-MM-dd",
                        value = """
                        {
                            "success": false,
                            "message": "Formato de fecha inválido. Use yyyy-MM-dd",
                            "data": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Rango muy amplio",
                        summary = "Período muy extenso",
                        description = "El rango de fechas es demasiado amplio",
                        value = """
                        {
                            "success": false,
                            "message": "El rango de fechas no puede exceder 12 meses",
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
                    description = "Error interno al generar el reporte",
                    value = """
                    {
                        "success": false,
                        "message": "Error al generar reporte: Database aggregation failed",
                        "data": null
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<ReporteEstadoCuentaDTO>> getReporteEstadoCuenta(
            @Parameter(
                description = "ID del cliente para generar el reporte", 
                required = true, 
                example = "1"
            )
            @RequestParam Long clienteId,
            
            @Parameter(
                description = "Fecha de inicio del período del reporte (formato: yyyy-MM-dd)", 
                required = true, 
                example = "2024-01-01"
            )
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            
            @Parameter(
                description = "Fecha de fin del período del reporte (formato: yyyy-MM-dd)", 
                required = true, 
                example = "2024-01-31"
            )
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {
        try {
            ReporteEstadoCuentaDTO reporte = reporteService.generarReporteEstadoCuenta(clienteId, fechaInicio, fechaFin);
            return ResponseEntity.ok(new ApiResponse<>(true, "Reporte generado exitosamente", reporte));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al generar reporte: " + e.getMessage(), null));
        }
    }
}