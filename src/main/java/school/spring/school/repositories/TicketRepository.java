package school.spring.school.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import school.spring.school.models.Ticket;
import school.spring.school.models.Ticket.Status;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	List<Ticket> findByClient_id(int id);
	List<Ticket> findByClient_username(String username);
	List<Ticket> findByDev_id(int id);
	List<Ticket> findByDev_username(String username);
	List<Ticket> findByStatus(Status s);
}
