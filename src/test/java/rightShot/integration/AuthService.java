package rightShot.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class AuthService {

	@Autowired
	private MockMvc mockMvc;
	
	//@formatter:off
		public MultiValueMap<String, String> getToken(){
			try {
				MultiValueMap<String, String> retorno = new LinkedMultiValueMap<>();
				MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
				params.add("grant_type", "password");
				params.add("username", "admin");
				params.add("password", "admin");

				MvcResult result = mockMvc
						.perform(post("/oauth/token")
								.header("Authorization", "Basic YWlyc29mdDphaXJzb2Z0")
								.params(params)
								.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
								.contentType(MediaType.APPLICATION_JSON))
						.andReturn();

				if (result.getResponse().getStatus() == HttpStatus.OK.value()) {
					JSONObject json = new JSONObject(result.getResponse().getContentAsString());
					retorno.add("Authorization", json.getString("token_type")
															.concat(" ")
															.concat(json.getString("access_token").toString()));
				}else {
					 throw new Exception("Não foi possivel autorizar o acesso.");
				}
				return retorno;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
}
