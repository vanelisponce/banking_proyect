package com.banking.cuenta.event;

import com.banking.cuenta.event.ClienteCreatedEvent;
import com.banking.cuenta.service.ClienteInfoService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventListener {

    @Autowired
    private ClienteInfoService clienteInfoService;

    @RabbitListener(queues = "cliente.created.queue")
    public void handleClienteCreated(ClienteCreatedEvent event) {
        try {
            // Actualizar información local del cliente para referencia
            clienteInfoService.actualizarInfoCliente(event);
            System.out.println("Cliente sincronizado: " + event.getClienteId() + " - " + event.getNombre());
        } catch (Exception e) {
            System.err.println("Error al procesar evento de cliente creado: " + e.getMessage());
        }
    }
}
