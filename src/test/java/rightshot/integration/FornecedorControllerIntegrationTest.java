package rightshot.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import rightshot.entity.Fornecedor;
import rightshot.repository.FornecedorRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
public class FornecedorControllerIntegrationTest {

   /* @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FornecedorRepository fornecedorRepository;

    @MockBean
    private EntityManager entityManagerMock;

    @Autowired
    private AuthService authService;

    MultiValueMap<String, String> heanders;

    @BeforeEach
    private void init() {
        heanders = this.authService.getToken();
    }

    @Test
    void salvarFornecedorTest_OK() throws JsonProcessingException, Exception {
        Fornecedor forn = new Fornecedor();
        forn.setRazaoSocial("Teste");

        Mockito.when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(forn);

        mockMvc.perform(post("/fornecedores/add")
                .content(objectMapper.writeValueAsString(forn))
                .header("Authorization", heanders.getFirst("Authorization"))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void buscarFornecedorPorId_OK() throws Exception {
        Fornecedor forn = new Fornecedor();
        forn.setId(1l);
        forn.setRazaoSocial("Teste");

        Mockito.when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(forn));

        mockMvc.perform(get("/fornecedores/one/1")
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void buscarFornecedorPorId_NotFound() throws Exception {

        Fornecedor forn = new Fornecedor();
        forn.setId(1l);
        forn.setRazaoSocial("Teste");

        Mockito.when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(forn));

        mockMvc.perform(get("/fornecedores/one/2")
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void buscarTodosFornededores_OK() throws Exception {
        mockMvc.perform(get("/fornecedores/all")
                .header("Authorization", heanders.getFirst("Authorization"))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void buscarTodosFornededoresPorRazaoSocial_OK() throws Exception {
        mockMvc.perform(get("/fornecedores/allFornecedorRazaoSocial")
                .header("Authorization", heanders.getFirst("Authorization"))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getFornecedorNewPage_OK() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("size", "1");
        params.add("page", "10");
        Fornecedor fornecedorFilterMock = new Fornecedor();

        PageRequest paginacao = PageRequest.of(1, 10);
        List<Fornecedor> fornecedorList = Arrays.asList(new Fornecedor(), new Fornecedor());
        Page<Fornecedor> fornecedorPage = new PageImpl<>(fornecedorList, paginacao, fornecedorList.size());

        Mockito.when(fornecedorRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(fornecedorPage);

        mockMvc.perform(get("/fornecedores/getAllFornecedorPageableNew")
                        //.header("Authorization", heanders.getFirst("Authorization"))
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fornecedorFilterMock))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(10));
    }*/

}
