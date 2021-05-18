package rightShot.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
