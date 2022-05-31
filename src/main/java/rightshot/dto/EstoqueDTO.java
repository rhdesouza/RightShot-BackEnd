package rightshot.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EstoqueDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id_produto;
	private String descricao;
	private Long qtd_nota;
	private Long qtd_estoque;
	private Long estoque_minimo;
	private String tipo;
	private String unid_compra;
	private String unid_venda;
	private Long qtd_venda;
	private Long qtd_est_real;

}
