package rightShot.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import rightShot.dao.PrecificacaoProdutoDao;
import rightShot.dto.PrecificacaoProdutoDTO;
import rightShot.dto.PrecificacaoProdutoHistoricoDTO;
import rightShot.dto.PrecificacaoProdutoListDTO;
import rightShot.entity.InfoRSC;
import rightShot.entity.PrecificacaoProduto;
import rightShot.entity.Produto;
import rightShot.repository.IInfoRSC;
import rightShot.repository.IPrecificacaoProduto;
import rightShot.repository.IProduto;
import rightShot.vo.PageVO;

@Slf4j
@Service
public class PrecificacaoProdutoService {
	
	public static final String MSG_ERRO_GET_PRODUTO = "Produto não localizado";

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private IPrecificacaoProduto iPrecificacaoProduto;

	@Autowired
	private UtilService utilService;

	@Autowired
	private PrecificacaoProdutoDao precificacaoProdutoDao;

	@Autowired
	private IProduto iProduto;

	@Autowired
	private IInfoRSC iInfoRSC;

	@Autowired
	EmailService emailService;

	public PageVO<PrecificacaoProdutoListDTO> getAllPrecificacaoProduto(PageVO<PrecificacaoProdutoListDTO> pageVO) {
		try {
			final TypedQuery<PrecificacaoProdutoListDTO> query = entityManager
					.createNamedQuery("PrecificacaoProdutoList", PrecificacaoProdutoListDTO.class);
			String where = "";

			String sqlQuery = query.unwrap(Query.class).getQueryString();
			sqlQuery = sqlQuery.concat(" ORDER BY " + pageVO.getSort() + " " + pageVO.getSortDirection());

			if (pageVO.getFilterForm() != null) {
				where = this.utilService.gerarWhereParaFiltro(pageVO.getFilterForm());
			}
			sqlQuery = sqlQuery.replace("HashWhereFilter", where);

			final javax.persistence.Query newQuery = entityManager.createNativeQuery(sqlQuery,
					"PrecificacaoProdutoListResultMapping");

			if (pageVO.isChangedQuery())
				pageVO.setTotalElements(this.utilService.CountQuery(newQuery, new HashMap<>()));

			newQuery.setFirstResult(pageVO.getPageIndex() * pageVO.getPageSize());
			newQuery.setMaxResults(pageVO.getPageSize());

			List<PrecificacaoProdutoListDTO> retorno = Collections.checkedList(newQuery.getResultList(),
					PrecificacaoProdutoListDTO.class);
			pageVO.setContent(retorno);

			return pageVO;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}

	}

	public PrecificacaoProdutoDTO buscarPrecificacaoPorIdProduto(Long idProduto) {
		try {
			PrecificacaoProdutoDTO ppDTO = new PrecificacaoProdutoDTO();
			ppDTO.setValorMedioPorProdutoVO(precificacaoProdutoDao.getSugestaoPrecificavaoProduto(idProduto));

			Double markup = iInfoRSC.findById(1).map(InfoRSC::getMarkup).filter(Objects::nonNull).orElse(0.0);

			Produto produto = iProduto.findById(idProduto)
					.orElseThrow(() -> new NotFoundException(MSG_ERRO_GET_PRODUTO));

			if (ppDTO.getValorMedioPorProdutoVO() != null) {
				ppDTO.getValorMedioPorProdutoVO().setMarkupRSC(markup);
				ppDTO.getValorMedioPorProdutoVO()
						.setValorSugerido(adicionarPercentual(ppDTO.getValorMedioPorProdutoVO().getValorMedio(),
								BigDecimal.valueOf(ppDTO.getValorMedioPorProdutoVO().getMarkupRSC())));
			}

			ppDTO.setProduto(produto);
			ppDTO.setPrecificacaoProduto(iPrecificacaoProduto.PrecificacaoProdutoPorIdProduto(idProduto));

			return ppDTO;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public BigDecimal adicionarPercentual(BigDecimal valor, BigDecimal percentual) {
		BigDecimal valorRetorno = (valor.multiply(percentual).divide(new BigDecimal(100))).add(valor);

		return valorRetorno;
	}

	public PrecificacaoProduto salvarPrecificacao(PrecificacaoProduto pp) {
		try {
			PrecificacaoProdutoDTO ppdto = this.buscarPrecificacaoPorIdProduto(pp.getProduto().getId());

			if ((ppdto.getValorMedioPorProdutoVO() != null && ppdto.getValorMedioPorProdutoVO().getValorMedio() != null
					&& pp.getValorProduto().compareTo(ppdto.getValorMedioPorProdutoVO().getValorMedio()) < 0))
				return null;

			if (ppdto.getValorMedioPorProdutoVO() != null) {
				pp.setMarkupReferncia(ppdto.getValorMedioPorProdutoVO().getMarkupRSC());
				pp.setValorMedioNF(ppdto.getValorMedioPorProdutoVO().getValorMedio());
				pp.setValorProdutoSugerido(ppdto.getValorMedioPorProdutoVO().getValorSugerido());
			}

			PrecificacaoProduto pps = iPrecificacaoProduto.save(pp);
			this.emailService.enviaEmailPrecificacao(pps);

			return pps;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public PrecificacaoProduto limparPrecificacaoProduto(Long idPrecificacao) {
		try {
			PrecificacaoProduto precificacaoProduto = iPrecificacaoProduto.findById(idPrecificacao)
					.orElseThrow(() -> new NotFoundException(MSG_ERRO_GET_PRODUTO));
			precificacaoProduto.setMarkupReferncia(0.0);
			precificacaoProduto.setValorMedioNF(null);
			precificacaoProduto.setValorProdutoSugerido(null);
			precificacaoProduto.setValorProduto(null);

			iPrecificacaoProduto.save(precificacaoProduto);
			return precificacaoProduto;

		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public PrecificacaoProdutoHistoricoDTO getHistoricoPrecificacaoProduto(Long idProduto) {
		try {
			Produto produto = iProduto.findById(idProduto)
					.orElseThrow(() -> new NotFoundException(MSG_ERRO_GET_PRODUTO));

			PrecificacaoProdutoHistoricoDTO pphDTO = new PrecificacaoProdutoHistoricoDTO(produto,
					this.precificacaoProdutoDao.getHistoricoPrecificacaoProduto(idProduto));

			return pphDTO;

		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

}
