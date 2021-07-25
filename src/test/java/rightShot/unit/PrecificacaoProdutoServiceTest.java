package rightShot.unit;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import rightShot.service.PrecificacaoProdutoService;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
public class PrecificacaoProdutoServiceTest {

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
        Assert.assertNotEquals(valor.toString(), new BigDecimal(120));
    }


}
