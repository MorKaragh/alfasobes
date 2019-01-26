package ru.alfasobes.alfasobes.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.alfasobes.alfasobes.model.InterviewQuestion;

@Repository
public interface InterviewQuestionRepository extends CrudRepository<InterviewQuestion, Long> {
}
