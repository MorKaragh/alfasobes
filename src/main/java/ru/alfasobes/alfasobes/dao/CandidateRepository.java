package ru.alfasobes.alfasobes.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alfasobes.alfasobes.model.Candidate;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate, Long> {

}
