package ru.alfasobes.alfasobes.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Interview interview;

    @Enumerated(EnumType.STRING)
    private InterviewAnswer answer;

}
