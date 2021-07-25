package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightShot.entity.TipoProduto;

import java.util.Optional;

@Repository
public interface TipoProdutoRepository extends JpaRepository<TipoProduto, Long> {

    Optional<TipoProduto> findByTipo(String tipo);

}
