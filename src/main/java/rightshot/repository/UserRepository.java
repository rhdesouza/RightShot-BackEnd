package rightshot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rightshot.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	User findByUser(String user);

}
