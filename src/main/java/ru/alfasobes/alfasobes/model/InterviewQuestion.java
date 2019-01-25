package ru.alfasobes.alfasobes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    public InterviewQuestion(Question question) {
        this.question = question;
    }

    @ManyToOne
    private Interview interview;

    @ManyToOne
    private Question question;

    @Enumerated(EnumType.STRING)
    private InterviewAnswer answer;

}
