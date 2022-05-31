package rightshot.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import rightshot.entity.NF;
import rightshot.entity.NfItens;
import rightshot.entity.SituacaoNF;
import rightshot.exception.RegraDeNegocioException;
import rightshot.repository.EstoqueRepository;
import rightshot.repository.INFRepository;
import rightshot.service.EmailService;
import rightshot.service.NFService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class NFServiceTest {

    @Autowired
    private NFService nfService;

    @MockBean
    private INFRepository infRepositoryMock;

    @MockBean
    private EmailService emailServiceMock;

    @MockBean
    private EstoqueRepository estoqueRepositoryMock;

    private NF nfMock;
    private List<NF> nfListMock;
    private List<NfItens> nfItensMock;

    @BeforeEach
    public void init() {
        nfMock = new NF();
        nfListMock = Arrays.asList(new NF(), new NF(), new NF(), new NF());
        nfItensMock = Arrays.asList(new NfItens(),new NfItens(),new NfItens());
    }

    @Test
    public void save_Ok() throws Exception {
        nfMock.setSituacao(SituacaoNF.Aberta);
        nfMock.setId(1L);
        Mockito.when(infRepositoryMock.save(nfMock)).thenReturn(nfMock);

        NF nfSaved = this.nfService.save(nfMock);
        Assertions.assertEquals(nfSaved, nfMock);
    }

    @Test
    public void save_enviaEmail() throws Exception {
        nfMock.setSituacao(SituacaoNF.Aberta);
        Mockito.when(infRepositoryMock.save(nfMock)).thenReturn(nfMock);
        Mockito.doNothing().when(emailServiceMock).enviaNotaFiscal(nfMock);

        NF nfSaved = this.nfService.save(nfMock);
        Assertions.assertEquals(nfSaved, nfMock);
    }

    @Test
    public void save_Exceptions() {
        nfMock.setSituacao(SituacaoNF.Estoque);
        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> this.nfService.save(nfMock));

        Assertions.assertEquals("Nota fiscal ja em estoque.", exception.getMessage());
    }

    @Test
    public void getAllNF_OK() {
        Mockito.when(infRepositoryMock.findAll()).thenReturn(nfListMock);

        List<NF> allNFReturn = this.nfService.getAllNF();

        Assertions.assertEquals(allNFReturn, nfListMock);
    }

    @Test
    public void getOneNotaFiscal_OK(){
        nfMock.setSituacao(SituacaoNF.Aberta);
        nfMock.setId(99L);
        Mockito.when(infRepositoryMock.findById(99L)).thenReturn(Optional.ofNullable(nfMock));

        NF NFReturnMock = this.nfService.getOneNotaFiscal(99l);

        Assertions.assertEquals(nfMock, NFReturnMock);
        Assertions.assertEquals(99l, NFReturnMock.getId());
    }

    @Test
    public void getOneNotaFiscal_Exceptions(){
        Mockito.when(infRepositoryMock.findById(99L)).thenReturn(Optional.empty());

        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> this.nfService.getOneNotaFiscal(99l));

        Assertions.assertEquals("Nota fiscal não localizada.", exception.getMessage());
    }

    @Test
    public void gerarEstoqueNF_OK(){
        nfMock.setSituacao(SituacaoNF.Aberta);
        nfMock.setId(99L);
        nfMock.setItens(nfItensMock);

        Mockito.when(infRepositoryMock.findById(99L)).thenReturn(Optional.ofNullable(nfMock));
        Mockito.when(estoqueRepositoryMock.saveAll(any(List.class))).thenReturn(null);
        Mockito.when(infRepositoryMock.save(nfMock)).thenReturn(nfMock);
        Mockito.doNothing().when(emailServiceMock).notificacaoGerarEstoque(nfMock);

        NF retornoService = nfService.gerarEstoqueNF(99L);

        Assertions.assertEquals(SituacaoNF.Estoque, retornoService.getSituacao());
    }

    @Test
    public void gerarEstoqueNF_Exception(){
        nfMock.setSituacao(SituacaoNF.Estoque);
        nfMock.setId(99L);
        Mockito.when(infRepositoryMock.findById(99L)).thenReturn(Optional.ofNullable(nfMock));

        RegraDeNegocioException exception = Assertions.assertThrows(RegraDeNegocioException.class,
                () -> this.nfService.gerarEstoqueNF(99l));

        Assertions.assertEquals("Nota fiscal não localiada, impossivel gerar estoque.", exception.getMessage());

    }

}
