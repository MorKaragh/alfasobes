package ru.alfasobes.alfasobes.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.alfasobes.alfasobes.model.Interview;

@Repository
public interface InterviewRepository extends CrudRepository<Interview,Long> {
}
