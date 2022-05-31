package rightshot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rightshot.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>{

	@Query("SELECT m FROM Menu m Where m.disable = 0 ORDER BY m.ordem")
	List<Menu> listarMenuAtivo();
	
}
