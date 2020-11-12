package com.itis.javalab.student.rabbitmq.documents.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.student.rabbitmq.documents.models.Person;
import com.itis.javalab.student.rabbitmq.documents.services.interfaces.BasicConsumer;
import com.itis.javalab.student.rabbitmq.documents.services.interfaces.ConfirmationService;
import com.itis.javalab.student.rabbitmq.documents.services.interfaces.ExchangeSender;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class BasicConsumerImpl implements BasicConsumer {
    private Connection connection;
    private final ConnectionFactory connectionFactory;
    private final ConfirmationService confirmationService;
    private final ObjectMapper objectMapper;
    private final ExchangeSender exchangeSender;

    private final String topicKey;
    private final String failKey;
    private final String queueName;

    public BasicConsumerImpl(ConnectionFactory connectionFactory, ConfirmationService confirmationService, ObjectMapper objectMapper, ExchangeSender exchangeSender,
                             @Value("${routing.key}") String topicKey,
                             @Value("${fail.routing.key}") String failKey,
                             @Value("${queue.name}") String queueName) {
        this.connectionFactory = connectionFactory;
        this.confirmationService = confirmationService;
        this.objectMapper = objectMapper;
        this.exchangeSender = exchangeSender;
        this.topicKey = topicKey;
        this.failKey = failKey;
        this.queueName = queueName;
    }

    @PostConstruct
    private void init() {
        try {
            connection = connectionFactory.newConnection();
            consume();
        } catch (IOException | TimeoutException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void consume() {
        try {
            Channel channel = connection.createChannel();
            channel.basicQos(1);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                Person person = objectMapper.readValue(message, Person.class);
                boolean confirmed = confirmationService.confirm(person);
                String key = confirmed ? topicKey : failKey;
                exchangeSender.sendMessages(key, person);
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
