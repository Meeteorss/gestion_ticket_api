package school.spring.school.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import school.spring.school.models.Incident;
@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

}