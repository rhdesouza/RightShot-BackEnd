package rightshot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClienteDTO implements Serializable {

	private Long id;
	private String nome;
	private String email;
	private String telefone;
	private String situacao;
	private Date created_date;

}
