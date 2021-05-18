package rightShot.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import rightShot.entity.PrecificacaoProduto;
import rightShot.repository.IPrecificacaoProduto;
import rightShot.vo.ValorMedioPorProdutoVO;

@Slf4j
@Component
public class PrecificacaoProdutoDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private IPrecificacaoProduto iPrecificacaoProduto;

	public ValorMedioPorProdutoVO getSugestaoPrecificavaoProduto(Long idProduto) {

		try {
			final TypedQuery<ValorMedioPorProdutoVO> consulta = entityManager
					.createNamedQuery("PrecificacaoProdutoPorIdSugerido", ValorMedioPorProdutoVO.class);
			consulta.setParameter("idProduto", idProduto);

			ValorMedioPorProdutoVO vmppvo = consulta.getSingleResult();

			return vmppvo;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}

	}

	public List<PrecificacaoProduto> getHistoricoPrecificacaoProduto(Long idProduto) {
		try {
			PrecificacaoProduto pp = iPrecificacaoProduto.PrecificacaoProdutoPorIdProduto(idProduto);
			
			List<PrecificacaoProduto> historico = 
					AuditReaderFactory.get(entityManager).createQuery()
					.forRevisionsOfEntity(PrecificacaoProduto.class, true, true)
					.add(AuditEntity.id().eq(pp.getId()))
					.getResultList();
		
			return historico;
		} catch (Exception e) {
			log.error(e.toString());
			return Collections.emptyList();
		}

	}

}
