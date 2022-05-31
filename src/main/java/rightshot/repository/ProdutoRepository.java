package rightshot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rightshot.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	  
	 @Query("SELECT p FROM Produto p WHERE p.id = :idProduto")
	 Produto buscarProdutoPorId(@Param(value = "idProduto") Long idProduto);
	 
}
