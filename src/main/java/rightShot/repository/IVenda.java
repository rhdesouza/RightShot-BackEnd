package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightShot.entity.Venda;

@Repository
public interface IVenda extends JpaRepository<Venda, Long> {
	
}
