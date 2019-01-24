package ru.alfasobes.alfasobes.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.alfasobes.alfasobes.model.Question;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
}
