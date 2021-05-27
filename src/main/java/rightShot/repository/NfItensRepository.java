package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightShot.entity.NfItens;

@Repository
public interface NfItensRepository extends JpaRepository<NfItens, Long> {

}
