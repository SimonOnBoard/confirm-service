package com.itis.javalab.student.rabbitmq.documents.services.interfaces;


import com.itis.javalab.student.rabbitmq.documents.models.Person;

public interface ExchangeSender {
    void sendMessages(String key, Person person);
}
