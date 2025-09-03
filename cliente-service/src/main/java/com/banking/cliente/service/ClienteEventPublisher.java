package com.banking.cliente.service;

import com.banking.cliente.entity.Cliente;
import com.banking.cliente.event.ClienteCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String CLIENTE_EXCHANGE = "cliente.exchange";
    private static final String CLIENTE_CREATED_ROUTING_KEY = "cliente.created";

    public void publishClienteCreado(Cliente cliente) {
        ClienteCreatedEvent event = new ClienteCreatedEvent();
        event.setClienteId(cliente.getClienteId());
        event.setNombre(cliente.getNombre());
        event.setIdentificacion(cliente.getIdentificacion());
        event.setEstado(cliente.getEstado());
        
        rabbitTemplate.convertAndSend(CLIENTE_EXCHANGE, CLIENTE_CREATED_ROUTING_KEY, event);
    }
}