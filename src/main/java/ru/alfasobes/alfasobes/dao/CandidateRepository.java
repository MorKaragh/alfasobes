package ru.alfasobes.alfasobes.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.alfasobes.alfasobes.model.Candidate;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate, Long> {
}
