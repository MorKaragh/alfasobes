package ru.alfasobes.alfasobes.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Interview> interviews = new ArrayList<>();

    public void addInterview(Interview interview) {
        interviews.add(interview);
    }
}
