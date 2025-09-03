package com.banking.cuenta.service;

import com.banking.cuenta.dto.ClienteInfoDTO;
import com.banking.cuenta.entity.ClienteInfo;
import com.banking.cuenta.event.ClienteCreatedEvent;
import com.banking.cuenta.repository.ClienteInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteInfoService {

    @Autowired
    private ClienteInfoRepository clienteInfoRepository;

    public ClienteInfoDTO getClienteInfo(Long clienteId) {
        Optional<ClienteInfo> clienteInfo = clienteInfoRepository.findByClienteId(clienteId);
        return clienteInfo.map(info -> {
            ClienteInfoDTO dto = new ClienteInfoDTO();
            dto.setClienteId(info.getClienteId());
            dto.setNombre(info.getNombre());
            dto.setIdentificacion(info.getIdentificacion());
            dto.setEstado(info.getEstado());
            return dto;
        }).orElse(null);
    }

    public void actualizarInfoCliente(ClienteCreatedEvent event) {
        ClienteInfo clienteInfo = new ClienteInfo();
        clienteInfo.setClienteId(event.getClienteId());
        clienteInfo.setNombre(event.getNombre());
        clienteInfo.setIdentificacion(event.getIdentificacion());
        clienteInfo.setEstado(event.getEstado());
        
        clienteInfoRepository.save(clienteInfo);
    }
}