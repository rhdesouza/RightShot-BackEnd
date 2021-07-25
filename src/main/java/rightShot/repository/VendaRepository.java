package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rightShot.entity.Venda;

import java.util.Optional;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>, JpaSpecificationExecutor<Venda> {

    @Query("SELECT v FROM Venda v JOIN FETCH v.cliente JOIN FETCH v.vendaItens Where v.id = :idVenda")
    Optional<Venda> findByIdVenda(@Param(value = "idVenda") Long idVenda);

}
