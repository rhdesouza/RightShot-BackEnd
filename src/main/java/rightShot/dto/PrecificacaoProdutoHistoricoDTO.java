package rightShot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightShot.entity.PrecificacaoProduto;
import rightShot.entity.Produto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecificacaoProdutoHistoricoDTO {

	private Produto produto;
	private List<PrecificacaoProduto> historicoPrecificacao;
	
}
