package rightshot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightshot.entity.PrecificacaoProduto;
import rightshot.entity.Produto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecificacaoProdutoHistoricoDTO {

	private Produto produto;
	private List<PrecificacaoProduto> historicoPrecificacao;
	
}
