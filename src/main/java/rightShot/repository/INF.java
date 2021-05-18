package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightShot.entity.NF;

@Repository
public interface INF extends JpaRepository<NF, Long>{

	
}
