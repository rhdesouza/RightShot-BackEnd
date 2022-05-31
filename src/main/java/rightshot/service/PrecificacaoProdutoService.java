package rightshot.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import rightshot.dao.PrecificacaoProdutoDao;
import rightshot.dto.PrecificacaoProdutoDTO;
import rightshot.dto.PrecificacaoProdutoHistoricoDTO;
import rightshot.dto.PrecificacaoProdutoListDTO;
import rightshot.entity.InfoRSC;
import rightshot.entity.PrecificacaoProduto;
import rightshot.entity.Produto;
import rightshot.repository.InfoRSCRepository;
import rightshot.repository.PrecificacaoProdutoRepository;
import rightshot.repository.ProdutoRepository;
import rightshot.vo.PageVO;

@Slf4j
@Service
public class PrecificacaoProdutoService {
	
	public static final String MSG_ERRO_GET_PRODUTO = "Produto não localizado";

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PrecificacaoProdutoRepository precificacaoProdutoRepository;

	@Autowired
	private UtilService utilService;

	@Autowired
	private PrecificacaoProdutoDao precificacaoProdutoDao;

	@Autowired
	private ProdutoRepository iProduto;

	@Autowired
	private InfoRSCRepository iInfoRSC;

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
			ppDTO.setPrecificacaoProduto(precificacaoProdutoRepository.PrecificacaoProdutoPorIdProduto(idProduto));

			return ppDTO;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public BigDecimal adicionarPercentual(@NotNull BigDecimal valor, @NotNull BigDecimal percentual) {
		return (valor.multiply(percentual).divide(new BigDecimal(100))).add(valor);
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

			PrecificacaoProduto pps = precificacaoProdutoRepository.save(pp);
			this.emailService.enviaEmailPrecificacao(pps);

			return pps;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public PrecificacaoProduto limparPrecificacaoProduto(Long idPrecificacao) {
		try {
			PrecificacaoProduto precificacaoProduto = precificacaoProdutoRepository.findById(idPrecificacao)
					.orElseThrow(() -> new NotFoundException(MSG_ERRO_GET_PRODUTO));
			precificacaoProduto.setMarkupReferncia(0.0);
			precificacaoProduto.setValorMedioNF(null);
			precificacaoProduto.setValorProdutoSugerido(null);
			precificacaoProduto.setValorProduto(null);

			precificacaoProdutoRepository.save(precificacaoProduto);
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

			return new PrecificacaoProdutoHistoricoDTO(produto,
					this.precificacaoProdutoDao.getHistoricoPrecificacaoProduto(idProduto));

		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

}
