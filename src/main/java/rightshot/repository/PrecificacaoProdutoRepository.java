package rightshot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import rightshot.entity.PrecificacaoProduto;

public interface PrecificacaoProdutoRepository extends CrudRepository<PrecificacaoProduto, Long> {

	@Query("select pp from PrecificacaoProduto pp where pp.produto.id = :idProduto")
	PrecificacaoProduto PrecificacaoProdutoPorIdProduto(@Param(value = "idProduto") Long idProduto);


}
