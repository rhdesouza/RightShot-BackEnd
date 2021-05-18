package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightShot.entity.TipoProduto;

@Repository
public interface ITipoProduto extends JpaRepository<TipoProduto, Long>{

	
}
