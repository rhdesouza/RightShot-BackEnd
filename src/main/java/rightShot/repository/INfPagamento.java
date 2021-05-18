package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightShot.entity.NfPagamento;

@Repository
public interface INfPagamento extends JpaRepository<NfPagamento, Long> {

}
