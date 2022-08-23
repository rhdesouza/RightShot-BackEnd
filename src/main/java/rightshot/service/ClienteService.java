package rightshot.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import rightshot.entity.Cliente;
import rightshot.entity.FotoCliente;
import rightshot.entity.SituacaoCliente;
import rightshot.exception.RegraDeNegocioException;
import rightshot.repository.ClientesRepository;
import rightshot.repository.FotoClienteRepository;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Slf4j
@Service
public class ClienteService {

    @Autowired
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

    public Cliente buscarClientePorId(Long idCliente) {
        return clienteRepository.findById(idCliente).orElse(null);
    }

    public String desativarCliente(Long idCliente) {
        try {
            Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> {
                throw new RegraDeNegocioException("Cliente não localizado");
            });
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

    public void regraClienteExistente(@NotNull Cliente cliente) {
        Optional<Cliente> cli = Optional.ofNullable(clienteRepository.findByCpf(cliente.getCpf()));
        if (cli.isPresent() && cliente.getId() == null && !cliente.getCpf().isEmpty())
            throw new RegraDeNegocioException("Já existe um cliente cadastrado para este CPF.");

    }

    public FotoCliente prepareSaveFoto(MultipartFile fotoCliente, Long idCliente) {
        try {
            Integer totalFotos = fotoClienteRepository.listarFotosPorCliente(idCliente).size();
            if (totalFotos > 2)
                throw new RegraDeNegocioException("Cada cliente poderá ter somente 3 fotos.");

            String fileName = StringUtils.cleanPath(fotoCliente.getOriginalFilename());

            return saveFoto(fotoCliente, idCliente, fileName);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RegraDeNegocioException(e.getMessage());
        }
    }

    private FotoCliente saveFoto(MultipartFile fotoCliente, Long idCliente, String fileName) throws Exception {
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
        } catch (MultipartException ex) {
            log.error(ex.getMessage());
            throw new MultipartException(ex.getMessage());

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new Exception("Could not store file " + fileName + ". Please try again!");
        }

    }

    public List<FotoCliente> buscarTodasFotosPorCliente(Long idCliente) {
        return fotoClienteRepository.listarFotosPorCliente(idCliente);
    }

    public FotoCliente getFotoCliente(Long idFotoCliente) {
        return fotoClienteRepository.findById(idFotoCliente).orElse(null);
    }

    public void deleteFotoCliente(Long idFotoCliente) {
        fotoClienteRepository.deleteById(idFotoCliente);
    }

    public Cliente getClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public Page<Cliente> getClientePageable(Cliente filter, Pageable pageable) {
        if (filter.getNome() == null && filter.getEmail() == null) {
            return clienteRepository.findAll(pageable);
        } else {
            ExampleMatcher matcher = ExampleMatcher
                    .matchingAll()
                    .withMatcher("nome", contains().ignoreCase())
                    .withMatcher("email", contains().ignoreCase());

            return clienteRepository.findAll(
                    Example.of(filter, matcher),
                    pageable);
        }
    }

}