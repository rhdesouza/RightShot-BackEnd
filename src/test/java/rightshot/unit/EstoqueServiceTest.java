package rightshot.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import rightshot.dto.EstoqueDTO;
import rightshot.entity.Estoque;
import rightshot.repository.EstoqueRepository;
import rightshot.service.EstoqueService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class EstoqueServiceTest {

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private EstoqueRepository estoqueRepositoryMock;

    @Autowired
    private EstoqueService estoqueService;

    private static final String ESTOQUE_SINTETICO = "EstoqueSintetico";
    List<EstoqueDTO> estoqueDTOListMock;

    @BeforeEach
    public void init() {

    }

    @Test
    public void getAllEstoqueSintetico_OK() throws Exception {
        estoqueDTOListMock = Arrays.asList(new EstoqueDTO(), new EstoqueDTO(), new EstoqueDTO(), new EstoqueDTO());
        TypedQuery<EstoqueDTO> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.createNamedQuery(ESTOQUE_SINTETICO, EstoqueDTO.class))
                .thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(estoqueDTOListMock);

        List<EstoqueDTO> retorno = estoqueService.getAllEstoqueSintetico();

        Assertions.assertEquals(estoqueDTOListMock, retorno);
    }

    @Test
    public void getAllEstoqueSintetico_Exception() throws Exception {
        Mockito.when(entityManager.createNamedQuery(ESTOQUE_SINTETICO, EstoqueDTO.class)).thenThrow(new NullPointerException());

        List<EstoqueDTO> retorno = estoqueService.getAllEstoqueSintetico();

        Assertions.assertEquals(Collections.emptyList(), retorno);
    }

    @Test
    public void getAllEstoque_OK(){
        List<Estoque> estoqueListMock = Arrays.asList(new Estoque(),new Estoque(),new Estoque());
        Mockito.when(estoqueRepositoryMock.findAll()).thenReturn(estoqueListMock);

        List<Estoque> retorno = estoqueService.getAllEstoque();

        Assertions.assertEquals(estoqueListMock, retorno);
    }

    @Test
    public void getAllEstoque_Exception(){
        Mockito.when(estoqueRepositoryMock.findAll()).thenThrow(new NullPointerException());

        List<Estoque> retorno = estoqueService.getAllEstoque();

        Assertions.assertEquals(Collections.emptyList(), retorno);
    }


}
