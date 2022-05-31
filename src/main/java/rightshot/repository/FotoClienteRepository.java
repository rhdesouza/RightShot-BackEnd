package rightshot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rightshot.entity.FotoCliente;

@Repository
public interface FotoClienteRepository extends JpaRepository<FotoCliente, Long> {
	
	@Query("SELECT fc FROM FotoCliente fc WHERE fc.cliente.id = :idCliente")
	List<FotoCliente> listarFotosPorCliente(@Param(value="idCliente") Long idCliente);

}
