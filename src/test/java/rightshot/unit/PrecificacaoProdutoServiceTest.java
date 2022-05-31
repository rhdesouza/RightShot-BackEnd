package rightshot.unit;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import rightshot.dao.PrecificacaoProdutoDao;
import rightshot.dto.PrecificacaoProdutoDTO;
import rightshot.dto.PrecificacaoProdutoHistoricoDTO;
import rightshot.entity.*;
import rightshot.repository.InfoRSCRepository;
import rightshot.repository.PrecificacaoProdutoRepository;
import rightshot.repository.ProdutoRepository;
import rightshot.service.PrecificacaoProdutoService;
import rightshot.vo.ValorMedioPorProdutoVO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class PrecificacaoProdutoServiceTest {

    @MockBean
    private PrecificacaoProdutoRepository precificacaoProdutoRepositoryMock;

    @MockBean
    private PrecificacaoProdutoDao precificacaoProdutoDaoMock;

    @MockBean
    private ProdutoRepository produtoRepositoryMock;

    @MockBean
    private InfoRSCRepository iInfoRSCMock;

    @Autowired
    private PrecificacaoProdutoService precificacaoProdutoService;

    //@DisplayName("Verifica se a função de adicionar o percentual está correta")
    @Test
    public void adicionarPercentual_Ok() {
        BigDecimal valor = precificacaoProdutoService.adicionarPercentual(
                new BigDecimal(100),
                new BigDecimal(10));
        Assert.assertEquals(valor, new BigDecimal(110));
    }

    @Test
    //@DisplayName("Verifica se a função de adicionar o percentual está correta")
    public void adicionarPercentual_Error() {
        BigDecimal valor = precificacaoProdutoService.adicionarPercentual(
                new BigDecimal(100),
                new BigDecimal(10));

        Assert.assertNotEquals(valor.toString(), new BigDecimal(120).toString());
    }

    @Test
    public void limparPrecificacaoProduto() {
        PrecificacaoProduto precificacaoProdutoMock = new PrecificacaoProduto(new Produto(), 12121,
                new BigDecimal(151), new BigDecimal(152), new BigDecimal(999));
        Mockito.when(precificacaoProdutoRepositoryMock.findById(1l)).thenReturn(Optional.of(precificacaoProdutoMock));
        Mockito.when(precificacaoProdutoRepositoryMock.save(precificacaoProdutoMock)).thenReturn(precificacaoProdutoMock);

        PrecificacaoProduto retorno = precificacaoProdutoService.limparPrecificacaoProduto(1l);

        Assertions.assertEquals(null, retorno.getValorProduto());
        Assertions.assertEquals(null, retorno.getValorProdutoSugerido());
        Assertions.assertEquals(null, retorno.getValorMedioNF());
        Assertions.assertEquals(0.0, retorno.getMarkupReferncia());

    }

    @Test
    public void limparPrecificacaoProduto_ProdutoNaoLocalizado() {
        Mockito.when(precificacaoProdutoRepositoryMock.findById(1l)).thenReturn(Optional.empty());

        PrecificacaoProduto retorno = precificacaoProdutoService.limparPrecificacaoProduto(1l);

        Assertions.assertNull(retorno);
    }


    @Test
    public void limparPrecificacaoProduto_Exception() {
        Mockito.when(precificacaoProdutoRepositoryMock.findById(1l)).thenThrow(new NullPointerException());

        PrecificacaoProduto retorno = precificacaoProdutoService.limparPrecificacaoProduto(1l);

        Assertions.assertNull(retorno);
    }

    @Test
    public void getHistoricoPrecificacaoProduto_OK() {
        Produto produtoMock = new Produto("11121", "Teste", new TipoProduto(), 151L, new Ncm());
        List<PrecificacaoProduto> precificacaoProdutoListMock = Arrays.asList(new PrecificacaoProduto(), new PrecificacaoProduto());
        Mockito.when(produtoRepositoryMock.findById(1l)).thenReturn(Optional.of(produtoMock));
        Mockito.when(precificacaoProdutoDaoMock.getHistoricoPrecificacaoProduto(1l)).thenReturn(precificacaoProdutoListMock);

        PrecificacaoProdutoHistoricoDTO retorno = precificacaoProdutoService.getHistoricoPrecificacaoProduto(1l);

        Assertions.assertEquals(retorno.getProduto(), produtoMock);
        Assertions.assertEquals(retorno.getHistoricoPrecificacao(), precificacaoProdutoListMock);
    }

    @Test
    public void getHistoricoPrecificacaoProduto_ProdutoNaoLocalizado() {
        Mockito.when(produtoRepositoryMock.findById(1l)).thenReturn(Optional.empty());

        PrecificacaoProdutoHistoricoDTO retorno = precificacaoProdutoService.getHistoricoPrecificacaoProduto(1l);

        Assertions.assertNull(retorno);
    }

    @Test
    public void getHistoricoPrecificacaoProduto_Exception() {
        Mockito.when(produtoRepositoryMock.findById(1l)).thenThrow(new NullPointerException());

        PrecificacaoProdutoHistoricoDTO retorno = precificacaoProdutoService.getHistoricoPrecificacaoProduto(1l);

        Assertions.assertNull(retorno);
    }

    @Test
    public void buscarPrecificacaoPorIdProduto_OK(){
        ValorMedioPorProdutoVO valorMedioPorProdutoVOMock = new ValorMedioPorProdutoVO(1l,10l, new BigDecimal(100), new BigDecimal(10));
        Mockito.when(precificacaoProdutoDaoMock.getSugestaoPrecificavaoProduto(1l)).thenReturn(valorMedioPorProdutoVOMock);
        InfoRSC infoRSCMock = new InfoRSC();
        infoRSCMock.setMarkup(10.0);
        Mockito.when(iInfoRSCMock.findById(1)).thenReturn(Optional.of(infoRSCMock));
        Produto produtoMock = new Produto("11111","Teste", new TipoProduto(), 100l, new Ncm());
        Mockito.when(produtoRepositoryMock.findById(1l)).thenReturn(Optional.of(produtoMock));

        PrecificacaoProdutoDTO retorno = precificacaoProdutoService.buscarPrecificacaoPorIdProduto(1l);

        Assertions.assertEquals(10.0, retorno.getValorMedioPorProdutoVO().getMarkupRSC());
        Assertions.assertEquals(produtoMock, retorno.getProduto());
        Assertions.assertEquals("11.0", retorno.getValorMedioPorProdutoVO().getValorSugerido().toString());
    }

    @Test
    public void buscarPrecificacaoPorIdProduto_Exeption(){
        ValorMedioPorProdutoVO valorMedioPorProdutoVOMock = new ValorMedioPorProdutoVO(1l,10l, new BigDecimal(100), new BigDecimal(10));
        Mockito.when(precificacaoProdutoDaoMock.getSugestaoPrecificavaoProduto(1l)).thenReturn(valorMedioPorProdutoVOMock);
        InfoRSC infoRSCMock = new InfoRSC();
        infoRSCMock.setMarkup(10.0);
        Mockito.when(iInfoRSCMock.findById(1)).thenReturn(Optional.of(infoRSCMock));
        Mockito.when(produtoRepositoryMock.findById(1l)).thenReturn(Optional.empty());

        PrecificacaoProdutoDTO retorno = precificacaoProdutoService.buscarPrecificacaoPorIdProduto(1l);

        Assertions.assertNull(retorno);
    }


}
