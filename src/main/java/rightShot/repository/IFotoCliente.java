package rightShot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rightShot.entity.FotoCliente;

@Repository
public interface IFotoCliente extends JpaRepository<FotoCliente, Long> {
	
	@Query("SELECT fc FROM FotoCliente fc WHERE fc.cliente.id = :idCliente")
	List<FotoCliente> listarFotosPorCliente(@Param(value="idCliente") Long idCliente);

}
