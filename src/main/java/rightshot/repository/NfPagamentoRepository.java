package rightshot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightshot.entity.NfPagamento;

@Repository
public interface NfPagamentoRepository extends JpaRepository<NfPagamento, Long> {

}
