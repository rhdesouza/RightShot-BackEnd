package rightShot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightShot.entity.PrecificacaoProduto;
import rightShot.entity.Produto;
import rightShot.vo.ValorMedioPorProdutoVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecificacaoProdutoDTO {

	private Produto produto;
	private ValorMedioPorProdutoVO valorMedioPorProdutoVO;
	private PrecificacaoProduto precificacaoProduto;

}
