package rightshot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightshot.entity.NfItens;

@Repository
public interface NfItensRepository extends JpaRepository<NfItens, Long> {

}
