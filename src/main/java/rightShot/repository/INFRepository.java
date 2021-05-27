package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightShot.entity.NF;

@Repository
public interface INFRepository extends JpaRepository<NF, Long>{

	
}
