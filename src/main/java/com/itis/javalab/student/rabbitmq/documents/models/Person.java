package com.itis.javalab.student.rabbitmq.documents.models;

import com.itis.javalab.student.rabbitmq.documents.utils.DatesComparator;
import com.rabbitmq.tools.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Person {
    @Id
    private String passport;

    private String name;
    private String patronymic;
    private String surname;

    @Transient
    private String mail;

    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(getPassport(), person.getPassport()) && Objects.equals(getName(), person.getName()) &&
                Objects.equals(getPatronymic(), person.getPatronymic()) &&
                Objects.equals(getSurname(), person.getSurname()) &&
                DatesComparator.compareDates(getDate(), person.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassport(), getName(), getPatronymic(), getSurname(), getMail(), getDate());
    }
}
