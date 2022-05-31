package rightshot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;
import rightshot.entity.Ncm;
import rightshot.entity.Produto;
import rightshot.entity.TipoProduto;
import rightshot.exception.RegraDeNegocioException;
import rightshot.repository.NcmRepository;
import rightshot.repository.ProdutoRepository;
import rightshot.repository.TipoProdutoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
public class ProdutoControllerIntegrationTest {

    /*@Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @MockBean
    private ProdutoRepository produtoRepositoryMock;

    @MockBean
    private TipoProdutoRepository tipoProdutoRepositoryMock;

    @MockBean
    private NcmRepository ncmRepositoryMock;

    @Autowired
    private ObjectMapper objectMapper;

    private MultiValueMap<String, String> heanders;
    private Produto produtoMock;
    private TipoProduto tipoProdutoMock;
    private Ncm ncmMock;

    @BeforeEach
    private void init() {
        heanders = this.authService.getToken();
        produtoMock = new Produto("010101", "Teste Produto", new TipoProduto(1l), 180l, new Ncm("1515"));
        tipoProdutoMock = new TipoProduto(1l);
        ncmMock = new Ncm("1515");
    }

    @Test
    public void addProduto_OK() throws Exception {
        Produto newProduto = new Produto("010101", "Teste Produto", new TipoProduto(1l), 180l, new Ncm("1515"));
        newProduto.setId(99l);

        Mockito.when(produtoRepositoryMock.save(newProduto)).thenReturn(newProduto);
        Mockito.when(tipoProdutoRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(tipoProdutoMock));
        Mockito.when(ncmRepositoryMock.findById(any(String.class))).thenReturn(Optional.of(ncmMock));

        mockMvc.perform(put("/produto/add")
                        .content(objectMapper.writeValueAsString(newProduto))
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("99"));
    }

    @Test
    public void addProduto_TipoProduto_Exception() throws Exception {
        Produto newProduto = new Produto("010101", "Teste Produto", new TipoProduto(1l), 180l, new Ncm("1515"));
        newProduto.setId(99l);

        Mockito.when(produtoRepositoryMock.save(newProduto)).thenReturn(newProduto);
        Mockito.when(tipoProdutoRepositoryMock.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/produto/add")
                        .content(objectMapper.writeValueAsString(newProduto))
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RegraDeNegocioException))
                .andExpect(result -> assertEquals("Tipo Produto não localizado", result.getResolvedException().getMessage()));
    }

    @Test
    public void addProduto_Ncm_Exception() throws Exception {
        Produto newProduto = new Produto("010101", "Teste Produto", new TipoProduto(1l), 180l, new Ncm("1515"));
        newProduto.setId(99l);

        Mockito.when(produtoRepositoryMock.save(newProduto)).thenReturn(newProduto);
        Mockito.when(tipoProdutoRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(tipoProdutoMock));
        Mockito.when(ncmRepositoryMock.findById(any(String.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/produto/add")
                        .content(objectMapper.writeValueAsString(newProduto))
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RegraDeNegocioException))
                .andExpect(result -> assertEquals("NCM não localizado", result.getResolvedException().getMessage()));
    }

    @Test
    public void getAllProdutos_OK() throws Exception {
        List<Produto> produtoList = new ArrayList<>();
        produtoList.add(new Produto());
        produtoList.add(new Produto());

        Mockito.when(produtoRepositoryMock.findAll()).thenReturn(produtoList);

        mockMvc.perform(get("/produto/all")
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getAllProdutos_NOT_FOUND() throws Exception {
        Mockito.when(produtoRepositoryMock.findAll()).thenReturn(null);
        mockMvc.perform(get("/produto/all")
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void buscarProdutoPorId_OK() throws Exception {
        Mockito.when(produtoRepositoryMock.buscarProdutoPorId(1L)).thenReturn(produtoMock);

        mockMvc.perform(get("/produto/one/{idProduto}", 1L)
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codProduto").value("010101"));
    }

    @Test
    public void buscarProdutoPorId_NotFound() throws Exception {
        Mockito.when(produtoRepositoryMock.buscarProdutoPorId(1L)).thenReturn(produtoMock);

        mockMvc.perform(get("/produto/one/{idProduto}", 2L)
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void buscarProdutoPorId_Exception() throws Exception {
        Mockito.when(produtoRepositoryMock.buscarProdutoPorId(1L)).thenThrow(IllegalStateException.class);

        mockMvc.perform(get("/produto/one/{idProduto}", 1)
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void desativarProduto_OK() throws Exception {
        Mockito.when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.of(produtoMock));
        Mockito.when(produtoRepositoryMock.save(produtoMock)).thenReturn(produtoMock);

        mockMvc.perform(post("/produto/desativar/{idProduto}", 1)
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataDesativacao").isNotEmpty());

    }

    @Test
    public void desativarProduto_Exception() throws Exception {
        Mockito.when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/produto/desativar/{idProduto}", 1)
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RegraDeNegocioException))
                .andExpect(result -> assertEquals("Produto não localizado.", result.getResolvedException().getMessage()));

    }*/
}
