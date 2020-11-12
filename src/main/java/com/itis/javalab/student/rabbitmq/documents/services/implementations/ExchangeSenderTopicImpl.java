package com.itis.javalab.student.rabbitmq.documents.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.student.rabbitmq.documents.models.Person;
import com.itis.javalab.student.rabbitmq.documents.services.interfaces.ExchangeSender;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Service
public class ExchangeSenderTopicImpl implements ExchangeSender {
    private final ConnectionFactory connectionFactory;

    private final String exchangeName;
    private final String exchangeType;

    public ExchangeSenderTopicImpl(ConnectionFactory connectionFactory,
                                   @Value("${exchange.name}") String exchangeName,
                                   @Value("${exchange.type}") String exchangeType,
                                   ObjectMapper objectMapper) {
        this.connectionFactory = connectionFactory;
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        this.objectMapper = objectMapper;
    }

    private final ObjectMapper objectMapper;
    private Connection connection;

    @PostConstruct
    public void setNewConnection() {
        try {
            connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, exchangeType);
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void sendMessages(String key, Person person) {
        try (Channel channel = connection.createChannel()) {
            channel.basicPublish(exchangeName, key,
                    null, objectMapper.writeValueAsBytes(person));
        } catch (IOException | TimeoutException e) {
            throw new IllegalStateException(e);
        }
    }
}
