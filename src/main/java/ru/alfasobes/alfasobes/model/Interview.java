package ru.alfasobes.alfasobes.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
public class Interview {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<InterviewQuestion> interviewQuestions = new LinkedList<>();

    private Date date;

}
