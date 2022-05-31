package rightshot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rightshot.entity.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

	@Query("SELECT f.id, f.razaoSocial FROM Fornecedor f ORDER BY f.razaoSocial")
	List<String> listarFornecedoresPorRazaoSocial();

}
