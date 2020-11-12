package com.itis.javalab.student.rabbitmq.documents.services.interfaces;

import com.itis.javalab.student.rabbitmq.documents.models.Person;

public interface ConfirmationService {
    boolean confirm(Person person);
}
