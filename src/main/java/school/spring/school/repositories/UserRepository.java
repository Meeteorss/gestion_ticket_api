package school.spring.school.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import school.spring.school.models.User;
import school.spring.school.models.User.Role;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
	List<User> findByRole(Role role);
}
