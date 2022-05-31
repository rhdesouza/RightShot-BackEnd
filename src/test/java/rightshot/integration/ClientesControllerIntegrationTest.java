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
import rightshot.entity.Cliente;
import rightshot.repository.ClientesRepository;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
public class ClientesControllerIntegrationTest {

    /*@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientesRepository clienteRepository;

    @Autowired
    private AuthService authService;

    private MultiValueMap<String, String> heanders;

    @BeforeEach
    private void init(){
       heanders = this.authService.getToken();
    }

    //@formatter:off
    @Test
    public void clinetesTestGetAll_OK() throws Exception {
        mockMvc.perform(get("/clientes/all")
                .header("Authorization", heanders.getFirst("Authorization"))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    //@formatter:on
    @Test
    void buscarClientePorId_OK() throws Exception {
        Cliente cli = new Cliente(1L, "Teste Cliente", "11122233344");
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cli));
        //Mockito.when(clienteRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(cli));

        mockMvc.perform(get("/clientes/one/{idCliente}",1)
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("11122233344"));

    }

    @Test
    void buscarClientePorId_NOT_FOUND() throws Exception {
        mockMvc.perform(get("/clientes/one/199")
                        .header("Authorization", heanders.getFirst("Authorization"))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void salvarClientesTest_OK() throws Exception {
        Cliente cli = new Cliente(1L, "Teste Cliente", "11122233344");

        Mockito.when(clienteRepository.save(cli)).thenReturn(cli);

        mockMvc.perform(post("/clientes/add")
                .content(objectMapper.writeValueAsString(cli))
                .header("Authorization", heanders.getFirst("Authorization"))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void salvarClientesTest_CONFLIT() throws Exception {
        Cliente oldCli = new Cliente(23L, "Teste Cliente OLD", "11122233344");
        Mockito.when(clienteRepository.findByCpf(oldCli.getCpf())).thenReturn(oldCli);

        Cliente newCli = new Cliente(1L, "Teste Cliente", "11122233344");
        Mockito.when(clienteRepository.save(newCli)).thenReturn(newCli);

        mockMvc.perform(post("/clientes/add")
                .content(objectMapper.writeValueAsString(newCli))
                .header("Authorization", heanders.getFirst("Authorization"))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }*/

}
