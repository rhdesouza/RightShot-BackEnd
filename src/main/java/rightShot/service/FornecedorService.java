package rightShot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import rightShot.dto.FornecedorDTO;
import rightShot.entity.Fornecedor;
import rightShot.entity.SituacaoFornecedor;
import rightShot.repository.IFornecedorDao;
import rightShot.vo.PageVO;

@Slf4j
@Service
public class FornecedorService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UtilService utilService;

	@Autowired
	IFornecedorDao iFornecedorDao;

	public List<Fornecedor> buscarTodosFornecedores() {
		return iFornecedorDao.findAll();
	}

	public PageVO<FornecedorDTO> getAllFornecedorPageable(PageVO<FornecedorDTO> pageVO) {
		try {
			final TypedQuery<FornecedorDTO> query = entityManager.createNamedQuery("FornecedorDTO",
					FornecedorDTO.class);
			String where = "";

			String sqlQuery = query.unwrap(Query.class).getQueryString();
			sqlQuery = sqlQuery.replace("HashWhereOrderBy",
					" ORDER BY " + pageVO.getSort() + " " + pageVO.getSortDirection());

			if (pageVO.getFilterForm() != null) {
				where = this.utilService.gerarWhereParaFiltro(pageVO.getFilterForm());
			}
			sqlQuery = sqlQuery.replace("HashWhereFilter", where);

			final javax.persistence.Query newQuery = entityManager.createNativeQuery(sqlQuery,
					"FornecedorResultMapping");

			if (pageVO.isChangedQuery())
				pageVO.setTotalElements(this.utilService.CountQuery(newQuery, new HashMap<>()));

			newQuery.setFirstResult(pageVO.getPageIndex() * pageVO.getPageSize());
			newQuery.setMaxResults(pageVO.getPageSize());

			List<FornecedorDTO> retorno = Collections.checkedList(newQuery.getResultList(), FornecedorDTO.class);
			pageVO.setContent(retorno);

			return pageVO;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return null;
	}

	public List<String> buscarTodosFornecedoresRazaoSocial() {
		return iFornecedorDao.listarFornecedoresPorRazaoSocial();
	}

	public Fornecedor buscarFornecedorPorId(Long idFornecedor) {
		try {
			return iFornecedorDao.findById(idFornecedor)
					.orElseThrow(()-> new NotFoundException("Fornecedor não localizado"));
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public Fornecedor save(Fornecedor fornecedor) {
		try {
			Fornecedor forn = iFornecedorDao.save(fornecedor);
			return forn;
		} catch (Exception ex) {
			log.error(ex.toString());
		}
		return fornecedor;
	}

	public boolean mokFornecedores() {
		if (!iFornecedorDao.existsById(1L)) {
			List<Fornecedor> fornecedores = new ArrayList<>();

			Fornecedor fonecedor1 = new Fornecedor("André e Renata Construções ME", "André e Renata Construções ME",
					"59833557000156", "1111111", SituacaoFornecedor.Ativo);
			fornecedores.add(fonecedor1);

			Fornecedor fonecedor2 = new Fornecedor("Fernando e Juliana Informática Ltda",
					"Fernando e Juliana Informática Ltda", "58669813000159", "2111111", SituacaoFornecedor.Ativo);
			fornecedores.add(fonecedor2);

			Fornecedor fonecedor3 = new Fornecedor("Patrícia e Henry Entulhos Ltda", "Patrícia e Henry Entulhos Ltda",
					"34005392000101", "3111111", SituacaoFornecedor.Ativo);
			fornecedores.add(fonecedor3);

			Fornecedor fonecedor4 = new Fornecedor("Agatha e Letícia Consultoria Financeira Ltda",
					"Agatha e Letícia Consultoria Financeira Ltda", "29335997000112", "1138974504",
					SituacaoFornecedor.Ativo);
			fornecedores.add(fonecedor4);

			Fornecedor fonecedor5 = new Fornecedor("AMADEO ROSSI IMP. E DIST. DE ARTIGOS ESPORTIVOS LTDA",
					"AMADEO ROSSI IMP. E DIST. DE ARTIGOS ESPORTIVOS LTDA", "96735105000168", "35912100",
					SituacaoFornecedor.Ativo);
			fornecedores.add(fonecedor5);

			iFornecedorDao.saveAll(fornecedores);
			return true;
		} else {
			return false;
		}
	}

}