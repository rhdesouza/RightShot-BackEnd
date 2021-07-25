package rightShot.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import rightShot.entity.Cliente;
import rightShot.repository.ClientesRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientesControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ClientesRepository clienteRepository;

	@Autowired
	private AuthService authService;
	
	//@formatter:off
	@Test
	public void clinetesTestGetAll_OK() throws Exception {
		MultiValueMap<String, String> heanders = this.authService.getToken();
		mockMvc.perform(get("/clientes/all")
							.header("Authorization", heanders.getFirst("Authorization"))
							.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk());
	}
	
	//@formatter:on
	@Test
	void buscarClientePorId_OK() throws Exception {
		MultiValueMap<String, String> heanders = this.authService.getToken();
		
		Cliente cli = new Cliente(1L, "Teste Cliente","11122233344");
		Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cli));

		mockMvc.perform(get("/clientes/one/1")
				.header("Authorization", heanders.getFirst("Authorization"))
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}
	
	@Test
	void buscarClientePorId_NOT_FOUND() throws Exception {
		MultiValueMap<String, String> heanders = this.authService.getToken();

		mockMvc.perform(get("/clientes/one/199")
				.header("Authorization", heanders.getFirst("Authorization"))
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}
	
	@Test 
	void salvarClientesTest_OK() throws Exception {
		MultiValueMap<String, String> heanders = this.authService.getToken();
		Cliente cli = new Cliente(1L, "Teste Cliente","11122233344");
		
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
		MultiValueMap<String, String> heanders = this.authService.getToken();
		
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
		
		
	}

}
