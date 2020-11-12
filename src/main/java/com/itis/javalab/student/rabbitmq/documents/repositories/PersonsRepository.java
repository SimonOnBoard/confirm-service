package com.itis.javalab.student.rabbitmq.documents.repositories;

import com.itis.javalab.student.rabbitmq.documents.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonsRepository  extends JpaRepository<Person, String> {
    Optional<Person> findByPassport(String id);
}
