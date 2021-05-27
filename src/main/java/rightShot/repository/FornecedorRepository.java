package rightShot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rightShot.entity.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

	@Query("SELECT f.id, f.razaoSocial FROM Fornecedor f ORDER BY f.razaoSocial")
	List<String> listarFornecedoresPorRazaoSocial();

}
