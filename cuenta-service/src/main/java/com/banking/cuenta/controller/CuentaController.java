package com.banking.cuenta.controller;

import com.banking.cuenta.dto.ApiResponse;
import com.banking.cuenta.dto.CrearCuentaDTO;
import com.banking.cuenta.dto.CuentaDTO;
import com.banking.cuenta.service.CuentaService;
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
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
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
    public ResponseEntity<ApiResponse<CuentaDTO>> getCuentaById(@PathVariable Long id) {
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
    public ResponseEntity<ApiResponse<CuentaDTO>> getCuentaByNumero(@PathVariable String numeroCuenta) {
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
    public ResponseEntity<ApiResponse<List<CuentaDTO>>> getCuentasByClienteId(@PathVariable Long clienteId) {
        try {
            List<CuentaDTO> cuentas = cuentaService.getCuentasByClienteId(clienteId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuentas del cliente obtenidas", cuentas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener cuentas: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CuentaDTO>> crearCuenta(@Valid @RequestBody CrearCuentaDTO crearCuentaDTO) {
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
    public ResponseEntity<ApiResponse<CuentaDTO>> actualizarCuenta(@PathVariable Long id,
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
    public ResponseEntity<ApiResponse<String>> eliminarCuenta(@PathVariable Long id) {
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