package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rightShot.entity.Cliente;

@Repository
public interface ClientesRepository extends JpaRepository<Cliente, Long> {
		
	Cliente findByCpf(String cpf);
	
}
