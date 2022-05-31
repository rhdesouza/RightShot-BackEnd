package rightshot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rightshot.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);

	@Query("SELECT u.id, u.name, u.modulo FROM Role u")
	List<String> getAllRoles();

	@Query("SELECT u FROM Role u WHERE u.name = :name")
	Role getOneRoleByName(@Param(value = "name") String name);

}
