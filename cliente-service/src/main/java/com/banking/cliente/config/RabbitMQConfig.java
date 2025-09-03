package com.banking.cliente.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLIENTE_EXCHANGE = "cliente.exchange";
    public static final String CLIENTE_CREATED_QUEUE = "cliente.created.queue";
    public static final String CLIENTE_CREATED_ROUTING_KEY = "cliente.created";

    @Bean
    public TopicExchange clienteExchange() {
        return new TopicExchange(CLIENTE_EXCHANGE);
    }

    @Bean
    public Queue clienteCreatedQueue() {
        return QueueBuilder.durable(CLIENTE_CREATED_QUEUE).build();
    }

    @Bean
    public Binding clienteCreatedBinding() {
        return BindingBuilder
                .bind(clienteCreatedQueue())
                .to(clienteExchange())
                .with(CLIENTE_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}