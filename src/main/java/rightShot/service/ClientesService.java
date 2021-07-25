package rightShot.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import rightShot.dto.ClienteDTO;
import rightShot.entity.Cliente;
import rightShot.entity.FotoCliente;
import rightShot.entity.SituacaoCliente;
import rightShot.exception.RegraDeNegocioException;
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
	ClientesRepository clienteRepository;

	@Autowired
	FotoClienteRepository fotoClienteRepository;

	public List<Cliente> buscarTodosClientes() {
		return clienteRepository.findAll();
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
			return clienteRepository.findById(idCliente).get();
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}

	}

	public String desativarCliente(Long idCliente) {
		try {
			Cliente cliente = clienteRepository.findById(idCliente).orElse(new Cliente());
			if (cliente.getSituacao() != SituacaoCliente.Inativo) {
				cliente.setSituacao(SituacaoCliente.Inativo);
				clienteRepository.save(cliente);
				return "Cliente Inativo";
			} else {
				throw new RegraDeNegocioException("Cliente ja esta inativo");
			}

		} catch (Exception e) {
			throw new RegraDeNegocioException(e.getMessage());
		}
	}

	public Cliente saveCliente(Cliente cliente) throws RegraDeNegocioException {
		try {
			regraClienteExistente(cliente);

			return clienteRepository.save(cliente);
		} catch (Exception e) {
			log.error(e.toString());
			throw new RegraDeNegocioException(e.getMessage());
		}

	}

	private void regraClienteExistente(@NotNull Cliente cliente){
		Optional<Cliente> cli = Optional.ofNullable(clienteRepository.findByCpf(cliente.getCpf()));
		if (cli.isPresent() && cliente.getId() == null)
			throw new RegraDeNegocioException("Já existe um cliente cadastrado para este CPF.");

	}

	public FotoCliente prepareSaveFoto(MultipartFile fotoCliente, Long idCliente) {
		try {
			Integer totalFotos = fotoClienteRepository.listarFotosPorCliente(idCliente).size();
			if (totalFotos > 2)
				throw new RegraDeNegocioException("Cada cliente poderá ter somente 3 fotos.");

			if (fotoCliente.getOriginalFilename() == null)
				throw new MultipartException("getOriginalFilename esta nulo");

			String fileName = StringUtils.cleanPath(fotoCliente.getOriginalFilename());

			return saveFoto(fotoCliente, idCliente, fileName);
		} catch (Exception e) {
			log.error(e.toString());
			throw new RegraDeNegocioException(e.getMessage());
		}
	}

	private FotoCliente saveFoto(MultipartFile fotoCliente, Long idCliente, String fileName) {
		try {
			FotoCliente fotoCli = new FotoCliente();
			if (fileName.contains("..")) {
				throw new MultipartException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			fotoCli.setFileName(fileName);
			fotoCli.setFileType(fotoCliente.getContentType());
			fotoCli.setSize(fotoCliente.getSize());
			fotoCli.setCliente(clienteRepository.findById(idCliente).orElse(new Cliente()));
			fotoCli.setDataCadastro(Calendar.getInstance());
			fotoCli.setData(fotoCliente.getBytes());
			fotoCli = fotoClienteRepository.save(fotoCli);

			return fotoCli;
		} catch (IOException ex) {
			throw new MultipartException("Could not store file " + fileName + ". Please try again!", ex);
		}

	}

	public List<FotoCliente> buscarTodasFotosPorCliente(Long idCliente) {
		return fotoClienteRepository.listarFotosPorCliente(idCliente);
	}

	public FotoCliente getFotoCliente(Long idFotoCliente) {
		try {
			FotoCliente fotoCli = fotoClienteRepository.findById(idFotoCliente).orElse(new FotoCliente());
			return fotoCli;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return null;
	}

	public void deleteFotoCliente(Long idFotoCliente) {
		try {
			fotoClienteRepository.deleteById(idFotoCliente);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	public Cliente getClientePorCpf(String cpf){
		return clienteRepository.findByCpf(cpf);

	}

}