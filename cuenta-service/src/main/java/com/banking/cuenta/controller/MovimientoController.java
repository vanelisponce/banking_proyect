package com.banking.cuenta.controller;

import com.banking.cuenta.dto.ApiResponse;
import com.banking.cuenta.dto.CrearMovimientoDTO;
import com.banking.cuenta.dto.MovimientoDTO;
import com.banking.cuenta.service.MovimientoService;
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
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<ApiResponse<MovimientoDTO>> crearMovimiento(@Valid @RequestBody CrearMovimientoDTO crearMovimientoDTO) {
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
    public ResponseEntity<ApiResponse<List<MovimientoDTO>>> getMovimientosPorCuenta(@PathVariable String numeroCuenta) {
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