package com.itis.javalab.student.rabbitmq.documents.services.implementations;

import com.itis.javalab.student.rabbitmq.documents.models.Person;
import com.itis.javalab.student.rabbitmq.documents.repositories.PersonsRepository;
import com.itis.javalab.student.rabbitmq.documents.services.interfaces.ConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final PersonsRepository personsRepository;

    @Override
    public boolean confirm(Person person) {
        Optional<Person> personCandidate = personsRepository.findByPassport(person.getPassport());
        if (personCandidate.isPresent()) {
            if(personCandidate.get().equals(person)){
                return true;
            }
        }

        return false;
    }
}
