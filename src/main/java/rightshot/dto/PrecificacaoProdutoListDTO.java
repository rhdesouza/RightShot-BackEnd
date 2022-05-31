package rightshot.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecificacaoProdutoListDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long idProduto;
	private String cod_produto;
	private String descricao;
	private String tipo;
	private String unid_compra;
	private String unid_venda;
	private Long id;
	private BigDecimal valor_produto;
	private Long qtd_est_real;

}
