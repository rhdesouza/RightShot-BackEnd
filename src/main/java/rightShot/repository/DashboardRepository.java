package rightShot.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class DashboardRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Object> getQtdAirsoftPorSituacao() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT situacao, COUNT(situacao) FROM tb_airsoft GROUP BY situacao");
		
		return entityManager.createNativeQuery(sql.toString()).getResultList();
	}
	
	public List<Object> getQtdClientesPorSituacao() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT situacao, COUNT(situacao) FROM tb_cliente GROUP BY situacao");
		
		return entityManager.createNativeQuery(sql.toString()).getResultList();
	}
	
	public List<Object> getQtdFornecedoresPorSituacao() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT situacao, COUNT(situacao) FROM tb_fornecedor GROUP BY situacao");
		
		return entityManager.createNativeQuery(sql.toString()).getResultList();
	}

}
