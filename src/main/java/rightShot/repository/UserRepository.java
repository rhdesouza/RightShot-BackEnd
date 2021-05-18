package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rightShot.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findByUser(String user);
}
