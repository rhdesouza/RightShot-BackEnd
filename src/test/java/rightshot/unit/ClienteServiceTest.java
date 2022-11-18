package rightshot.unit;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import rightshot.dto.ClienteDTO;
import rightshot.entity.Cliente;
import rightshot.entity.FotoCliente;
import rightshot.entity.SituacaoCliente;
import rightshot.exception.RegraDeNegocioException;
import rightshot.repository.ClientesRepository;
import rightshot.repository.FotoClienteRepository;
import rightshot.service.ClienteService;
import rightshot.vo.PageVO;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteServiceTest {

    @Autowired
    private ClienteService clienteService;

    @MockBean
    private ClientesRepository clienteRepository;

    @MockBean
    private FotoClienteRepository fotoClienteRepository;

    @MockBean
    private EntityManager entityManager;

    private Cliente clienteMock;

    private MultipartFile multipartFileMock;

    private FotoCliente fotoClienteMock;

    private PageVO<ClienteDTO> pageVOMock;

    private EasyRandom easyRandom;

    @BeforeEach
    void preparaMock() {
        easyRandom = new EasyRandom();
        clienteMock = easyRandom.nextObject(Cliente.class);
        multipartFileMock = new MockMultipartFile("Teste", "Teste.txt", MediaType.TEXT_PLAIN_VALUE, "TesteArquivo".getBytes());
        fotoClienteMock = new FotoCliente("teste", "teste", "teste".getBytes());

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
        Mockito.when(clienteRepository.findByCpf("11122233344")).thenReturn(clienteMock);
        Mockito.when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(fotoClienteRepository.save(any(FotoCliente.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(fotoClienteRepository.findById(1L)).thenReturn(Optional.of(fotoClienteMock));

    }
    @Test
    void buscarClientePorId_quandoLocalizado() {
        Cliente cliente = clienteService.buscarClientePorId(1l);
        Assertions.assertEquals(cliente, clienteMock);
    }

    @Test
    void buscarClientePorId_quandoNaoLocalizado() {
        Cliente cliente = clienteService.buscarClientePorId(2l);
        Assertions.assertNull(cliente);
    }

    @Test
    void desativarCliente_quandoForSucesso() {
        String retorno = clienteService.desativarCliente(1l);
        Assertions.assertEquals("Cliente Inativo", retorno);
    }

    @Test
    void desativarCliente_quandoNaoLocalizarCliente() {
        Mockito.when(clienteRepository.findById(3l)).thenReturn(Optional.empty());

        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> clienteService.desativarCliente(3l));

        Assertions.assertEquals("Cliente não localizado", exception.getMessage());
    }

    @Test
    void desativarCliente_quandoClienteEstaInativo() {
        clienteMock.setSituacao(SituacaoCliente.Inativo);

        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> clienteService.desativarCliente(1l));

        Assertions.assertEquals("Cliente ja esta inativo", exception.getMessage());
    }

    @Test
    void saveCliente_quandoClienteEstaCorreto() {
        Cliente newCliente = new Cliente(2l, "Teste2", "11122233344");
        Cliente cliente = clienteService.saveCliente(newCliente);

        Assertions.assertEquals(cliente, newCliente);
    }

    @Test
    void saveCliente_quandoClienteJaExiste() {
        Cliente newCliente = new Cliente(null, "NovoTeste", "11122233344");

        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> clienteService.saveCliente(newCliente));

        Assertions.assertEquals("Já existe um cliente cadastrado para este CPF.", exception.getMessage());
    }

    @Test
    void prepareSaveFoto_quandoArquivoEClienteExsite() {
        Mockito.when(fotoClienteRepository.listarFotosPorCliente(1l)).thenReturn(new ArrayList<FotoCliente>());

        FotoCliente fotoCliente = clienteService.prepareSaveFoto(multipartFileMock, 1l);
        Assertions.assertNotNull(fotoCliente);
    }

    @Test
    void prepareSaveFoto_quandoClienteTiverMaisQueTresFotos() {
        Mockito.when(fotoClienteRepository.listarFotosPorCliente(1l))
                .thenReturn(new ArrayList<FotoCliente>(
                        Arrays.asList(
                                new FotoCliente("teste", "teste", "teste".getBytes()),
                                new FotoCliente("teste", "teste", "teste".getBytes()),
                                new FotoCliente("teste", "teste", "teste".getBytes())
                        )
                ));
        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> clienteService.prepareSaveFoto(multipartFileMock, 1l));

        Assertions.assertEquals("Cada cliente poderá ter somente 3 fotos.", exception.getMessage());
    }

    @Test
    void prepareSaveFoto_quandoNomeArquivoForInvalido() {
        Mockito.when(fotoClienteRepository.listarFotosPorCliente(1l))
                .thenReturn(new ArrayList<FotoCliente>(
                        Arrays.asList(new FotoCliente("teste", "teste", "teste".getBytes()))
                ));

        MultipartFile multipartFileMocks = new MockMultipartFile("Teste", "Teste...txt", MediaType.TEXT_PLAIN_VALUE, "TesteArquivo".getBytes());

        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> clienteService.prepareSaveFoto(multipartFileMocks, 1l));

        Assertions.assertEquals("Sorry! Filename contains invalid path sequence " + multipartFileMocks.getOriginalFilename(), exception.getMessage());
    }

    @Test
    void prepareSaveFoto_quandoRetornarUmaExceptionAoSalvarArquivo() {
        Mockito.when(fotoClienteRepository.listarFotosPorCliente(1l))
                .thenReturn(new ArrayList<FotoCliente>(
                        Arrays.asList(new FotoCliente("teste", "teste", "teste".getBytes()))
                ));

        Mockito.when(fotoClienteRepository.save(any(FotoCliente.class))).thenThrow(new NullPointerException());

        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> clienteService.prepareSaveFoto(multipartFileMock, 1l));

        Assertions.assertEquals("Could not store file " + multipartFileMock.getOriginalFilename() + ". Please try again!", exception.getMessage());
    }

    @Test
    void buscarTodasFotosPorCliente_quandoClienteExiste() {
        Mockito.when(fotoClienteRepository.listarFotosPorCliente(1l))
                .thenReturn(new ArrayList<FotoCliente>(
                        Arrays.asList(
                                new FotoCliente("teste", "teste", "teste".getBytes()),
                                new FotoCliente("teste", "teste", "teste".getBytes()),
                                new FotoCliente("teste", "teste", "teste".getBytes())
                        )
                ));
        List<FotoCliente> fotoClientes = clienteService.buscarTodasFotosPorCliente(1l);

        Assertions.assertEquals(3, fotoClientes.size());

    }

    @Test
    void buscarTodasFotosPorCliente_quandoClienteNaoExiste() {
        List<FotoCliente> fotoClientes = clienteService.buscarTodasFotosPorCliente(1l);
        Assertions.assertEquals(0, fotoClientes.size());
    }

    @Test
    void getFotoCliente_quandoClienteExiste() {
        FotoCliente fotoCliente = clienteService.getFotoCliente(1L);
        Assertions.assertNotNull(fotoCliente);
    }

    @Test
    void getFotoCliente_quandoClienteNaoExiste() {
        FotoCliente fotoCliente = clienteService.getFotoCliente(2L);
        Assertions.assertNull(fotoCliente);
    }

    @Test
    void deleteFotoCliente_quandoClienteExiste() {
        clienteService.deleteFotoCliente(1l);
        Mockito.verify(fotoClienteRepository, Mockito.times(1)).deleteById(1l);
    }

    @Test
    void getClientePageable_quandoRetornaFiltrado() {
        Cliente filter = new Cliente();
        filter.setNome("TesteMock");
        List<Cliente> clienteList = Arrays.asList(new Cliente(), new Cliente(), new Cliente());
        Pageable pageable = PageRequest.of(1, 10);
        Page<Cliente> pageCli = new PageImpl<Cliente>(clienteList, pageable, clienteList.size());

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("nome", contains().ignoreCase())
                .withMatcher("email", contains().ignoreCase());

        Mockito.when(clienteRepository.findAll(
                        Example.of(filter, matcher),
                        pageable))
                .thenReturn(pageCli);

        Page<Cliente> retorno = this.clienteService.getClientePageable(filter, pageable);

        Assertions.assertEquals(retorno.getContent(), clienteList);
        Assertions.assertEquals(retorno.getPageable().getPageSize(), 10);
        Assertions.assertEquals(retorno.getPageable().getPageNumber(), 1);

    }

    @Test
    void getClientePageable_quandoQuandoNaoExisteFiltro() {
        Cliente filter = null;
        List<Cliente> clienteList = Arrays.asList(new Cliente(), new Cliente(), new Cliente());
        Pageable pageable = PageRequest.of(1, 10);
        Page<Cliente> pageCli = new PageImpl<Cliente>(clienteList, pageable, clienteList.size());

        Mockito.when(clienteRepository.findAll(pageable))
                .thenReturn(pageCli);

        Page<Cliente> retorno = this.clienteService.getClientePageable(filter, pageable);

        Assertions.assertEquals(retorno.getContent(), clienteList);
        Assertions.assertEquals(retorno.getPageable().getPageSize(), 10);
        Assertions.assertEquals(retorno.getPageable().getPageNumber(), 1);
    }

    @Test
    void getClientePageable_quandoOFiltroEstaVazio() {
        Cliente filter = new Cliente();
        filter.setNome(null);
        filter.setEmail(null);
        List<Cliente> clienteList = Arrays.asList(new Cliente(), new Cliente(), new Cliente());
        Pageable pageable = PageRequest.of(1, 10);
        Page<Cliente> pageCli = new PageImpl<Cliente>(clienteList, pageable, clienteList.size());

        Mockito.when(clienteRepository.findAll(pageable))
                .thenReturn(pageCli);

        Page<Cliente> retorno = this.clienteService.getClientePageable(filter, pageable);

        Assertions.assertEquals(retorno.getContent(), clienteList);
        Assertions.assertEquals(retorno.getPageable().getPageSize(), 10);
        Assertions.assertEquals(retorno.getPageable().getPageNumber(), 1);
    }

    @Test
    void buscarTodosClientes_quandoChamado() {
        List<Cliente> clienteList = Arrays.asList(new Cliente(), new Cliente(), new Cliente());
        Mockito.when(clienteRepository.findAll())
                .thenReturn(clienteList);

        List<Cliente> retorno = this.clienteService.buscarTodosClientes();

        Assertions.assertEquals(retorno, clienteList);
    }

}
