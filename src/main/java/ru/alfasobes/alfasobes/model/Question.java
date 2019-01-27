package ru.alfasobes.alfasobes.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "question", nullable = false, length = 200)
    private String question;

    @Column(name = "hint", length = 4000)
    private String hint;

    @Column(name = "categories", nullable = false, length = 1000)
    private String categories;

}
