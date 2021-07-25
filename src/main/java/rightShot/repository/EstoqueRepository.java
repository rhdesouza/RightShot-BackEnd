package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rightShot.entity.Estoque;

public interface EstoqueRepository extends JpaRepository<Estoque, Long>{

    @Query("SELECT est FROM Estoque est WHERE est.produto.id = :idProduto")
    Estoque findByProduto(@Param(value = "idProduto") Long idProduto);

}
