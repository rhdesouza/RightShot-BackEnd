package rightShot.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FornecedorDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String razao_social;
	private String nome_fantasia;
	private String cpf_cnpj;
	private String telefone;
	private String situacao;

}
