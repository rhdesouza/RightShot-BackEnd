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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import rightShot.entity.Cliente;
import rightShot.entity.Fornecedor;
import rightShot.repository.FornecedorRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class FornecedorControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private AuthService authService;
	
	@Test
	void salvarFornecedorTest_OK() throws JsonProcessingException, Exception {
		MultiValueMap<String, String> heanders = this.authService.getToken();
		Fornecedor forn = new Fornecedor();
		forn.setRazaoSocial("Teste");
		
		Mockito.when(fornecedorRepository.save(Mockito.any(Fornecedor.class))).thenReturn(forn);
		
		mockMvc.perform(post("/fornecedores/add")
				.content(objectMapper.writeValueAsString(forn))
				.header("Authorization", heanders.getFirst("Authorization"))
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk());
	}
	
	@Test
	void buscarFornecedorPorId_OK() throws Exception {
		MultiValueMap<String, String> heanders = this.authService.getToken();
		
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
		MultiValueMap<String, String> heanders = this.authService.getToken();
		
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
		MultiValueMap<String, String> heanders = this.authService.getToken();
		mockMvc.perform(get("/fornecedores/all")
							.header("Authorization", heanders.getFirst("Authorization"))
							.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk());
	}
	
	@Test
	public void buscarTodosFornededoresPorRazaoSocial_OK() throws Exception {
		MultiValueMap<String, String> heanders = this.authService.getToken();
		mockMvc.perform(get("/fornecedores/allFornecedorRazaoSocial")
							.header("Authorization", heanders.getFirst("Authorization"))
							.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk());
	}

}
