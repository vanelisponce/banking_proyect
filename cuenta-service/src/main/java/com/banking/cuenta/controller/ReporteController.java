package com.banking.cuenta.controller;

import com.banking.cuenta.dto.ApiResponse;
import com.banking.cuenta.dto.ReporteEstadoCuentaDTO;
import com.banking.cuenta.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public ResponseEntity<ApiResponse<ReporteEstadoCuentaDTO>> getReporteEstadoCuenta(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
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