package rightshot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightshot.entity.PrecificacaoProduto;
import rightshot.entity.Produto;
import rightshot.vo.ValorMedioPorProdutoVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecificacaoProdutoDTO {

	private Produto produto;
	private ValorMedioPorProdutoVO valorMedioPorProdutoVO;
	private PrecificacaoProduto precificacaoProduto;

}
