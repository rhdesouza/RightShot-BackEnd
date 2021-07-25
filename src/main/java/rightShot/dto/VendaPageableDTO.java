package rightShot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendaPageableDTO implements Serializable {

	private Long idVenda;
	private Date dataHoraVenda;
	private Boolean emailEnviado;
	private String cpf;
	private String nome;
	private String email;
	private Integer situacaoVenda;

}
