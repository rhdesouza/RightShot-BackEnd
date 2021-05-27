package rightShot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rightShot.entity.Ncm;

@Repository
public interface NcmRepository extends JpaRepository<Ncm, String> {

	@Query("SELECT n FROM Ncm n WHERE n.ncm like :ncm%")
	List<Ncm> listarNcmPorIdLike(@Param(value = "ncm") String ncm);

}
