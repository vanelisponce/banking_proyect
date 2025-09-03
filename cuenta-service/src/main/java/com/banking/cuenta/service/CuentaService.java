package com.banking.cuenta.service;

import com.banking.cuenta.dto.CrearCuentaDTO;
import com.banking.cuenta.dto.CuentaDTO;
import com.banking.cuenta.entity.Cuenta;
import com.banking.cuenta.repository.CuentaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClienteInfoService clienteInfoService;

    public List<CuentaDTO> getAllCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        return cuentas.stream()
                .map(cuenta -> modelMapper.map(cuenta, CuentaDTO.class))
                .collect(Collectors.toList());
    }

    public CuentaDTO getCuentaById(Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        return cuenta.map(c -> modelMapper.map(c, CuentaDTO.class)).orElse(null);
    }

    public CuentaDTO getCuentaByNumero(String numeroCuenta) {
        Optional<Cuenta> cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta);
        return cuenta.map(c -> modelMapper.map(c, CuentaDTO.class)).orElse(null);
    }

    public List<CuentaDTO> getCuentasByClienteId(Long clienteId) {
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        return cuentas.stream()
                .map(cuenta -> modelMapper.map(cuenta, CuentaDTO.class))
                .collect(Collectors.toList());
    }

    public CuentaDTO crearCuenta(CrearCuentaDTO crearCuentaDTO) {
        // Validar si ya existe el número de cuenta
        if (cuentaRepository.existsByNumeroCuenta(crearCuentaDTO.getNumeroCuenta())) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese número");
        }

        // Validar que el cliente existe
        if (clienteInfoService.getClienteInfo(crearCuentaDTO.getClienteId()) == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(crearCuentaDTO.getNumeroCuenta());
        cuenta.setTipoCuenta(crearCuentaDTO.getTipoCuenta());
        cuenta.setSaldoInicial(crearCuentaDTO.getSaldoInicial());
        cuenta.setEstado(true);
        cuenta.setClienteId(crearCuentaDTO.getClienteId());

        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return modelMapper.map(cuentaGuardada, CuentaDTO.class);
    }

    public CuentaDTO actualizarCuenta(Long id, CrearCuentaDTO cuentaDTO) {
        Optional<Cuenta> cuentaExistente = cuentaRepository.findById(id);
        if (cuentaExistente.isEmpty()) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        Cuenta cuenta = cuentaExistente.get();
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return modelMapper.map(cuentaActualizada, CuentaDTO.class);
    }

    public boolean eliminarCuenta(Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        if (cuenta.isPresent()) {
            // Soft delete - cambiar estado a false
            Cuenta cuentaEntity = cuenta.get();
            cuentaEntity.setEstado(false);
            cuentaRepository.save(cuentaEntity);
            return true;
        }
        return false;
    }
}