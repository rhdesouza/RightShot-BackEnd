package rightShot.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import rightShot.dto.ClienteDTO;
import rightShot.entity.Cliente;
import rightShot.entity.FotoCliente;
import rightShot.entity.SituacaoCliente;
import rightShot.repository.ClientesRepository;
import rightShot.repository.FotoClienteRepository;
import rightShot.vo.PageVO;

@Slf4j
@Service
public class ClientesService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UtilService utilService;

	@Autowired
	ClientesRepository iClientesDao;

	@Autowired
	FotoClienteRepository iFotoCliente;

	public List<Cliente> buscarTodosClientes() {
		return iClientesDao.findAll();
	}

	@SuppressWarnings("unchecked")
	public PageVO<ClienteDTO> getAllClientePageable(PageVO<ClienteDTO> pageVO) {
		try {
			final TypedQuery<ClienteDTO> query = entityManager.createNamedQuery("ClienteDTO", ClienteDTO.class);
			String where = "";

			String sqlQuery = query.unwrap(Query.class).getQueryString();
			sqlQuery = sqlQuery.replace("HashWhereOrderBy",
					" ORDER BY " + pageVO.getSort() + " " + pageVO.getSortDirection());

			if (pageVO.getFilterForm() != null) {
				where = this.utilService.gerarWhereParaFiltro(pageVO.getFilterForm());
			}
			sqlQuery = sqlQuery.replace("HashWhereFilter", where);

			final javax.persistence.Query newQuery = entityManager.createNativeQuery(sqlQuery,
					"ClienteDTOResultMapping");

			if (pageVO.isChangedQuery())
				pageVO.setTotalElements(this.utilService.CountQuery(newQuery, new HashMap<>()));

			newQuery.setFirstResult(pageVO.getPageIndex() * pageVO.getPageSize());
			newQuery.setMaxResults(pageVO.getPageSize());

			List<ClienteDTO> retorno = Collections.checkedList(newQuery.getResultList(), ClienteDTO.class);
			pageVO.setContent(retorno);

			return pageVO;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public Cliente buscarClientePorId(Long idCliente) {
		try {
			return iClientesDao.findById(idCliente).get();
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}

	}

	public ResponseEntity<String> desativarCliente(Long idCliente) {
		try {
			Cliente cliente = iClientesDao.findById(idCliente).get();
			if (cliente.getSituacao() != SituacaoCliente.Inativo) {
				cliente.setSituacao(SituacaoCliente.Inativo);
				iClientesDao.save(cliente);
				return new ResponseEntity<>("Cliente Inativo", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Cliente ja esta inativo", HttpStatus.CONFLICT);
			}

		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	public Cliente save(Cliente cliente) {
		try {
			return iClientesDao.save(cliente);
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public ResponseEntity<?> saveFoto(MultipartFile fotoCliente, Long idCliente) {
		Integer totalFotos = iFotoCliente.listarFotosPorCliente(idCliente).size();
		if (totalFotos > 2) {

			return new ResponseEntity<>("erro:1, msg:Cada cliente poderá ter somente 3 fotos", HttpStatus.CONFLICT);
			/*
			 * throw new
			 * MultipartException("Cada cliente poderá ter somente 3 fotos cadastradas;");
			 */
		}

		try {
			String fileName = StringUtils.cleanPath(fotoCliente.getOriginalFilename());
			FotoCliente fotoCli = new FotoCliente();
			try {
				if (fileName.contains("..")) {
					throw new MultipartException("Sorry! Filename contains invalid path sequence " + fileName);
				}

				fotoCli.setFileName(fileName);
				fotoCli.setFileType(fotoCliente.getContentType());
				fotoCli.setSize(fotoCliente.getSize());
				fotoCli.setCliente(iClientesDao.findById(idCliente).get());
				fotoCli.setDataCadastro(Calendar.getInstance());
				fotoCli.setData(fotoCliente.getBytes());
				fotoCli = iFotoCliente.save(fotoCli);

			} catch (IOException ex) {
				throw new MultipartException("Could not store file " + fileName + ". Please try again!", ex);
			}

			return new ResponseEntity<>(fotoCli, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	public List<FotoCliente> buscarTodasFotosPorCliente(Long idCliente) {
		return iFotoCliente.listarFotosPorCliente(idCliente);
	}

	public ResponseEntity<FotoCliente> getFotoCliente(Long idFotoCliente) {
		try {
			FotoCliente fotoCli = iFotoCliente.findById(idFotoCliente).get();
			return new ResponseEntity<FotoCliente>(fotoCli, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<?> deleteFotoCliente(Long idFotoCliente) {
		try {
			iFotoCliente.deleteById(idFotoCliente);
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

}