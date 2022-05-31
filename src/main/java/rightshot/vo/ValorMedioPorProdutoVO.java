package rightshot.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValorMedioPorProdutoVO {

	private Long idProduto;
	private Long totalQtd;
	private BigDecimal sumValorTotal;
	private BigDecimal valorMedio;
	private double markupRSC;
	private BigDecimal valorSugerido;
	
	public ValorMedioPorProdutoVO(Long idProduto, Long totalQtd, BigDecimal sumValorTotal, BigDecimal valorMedio) {
		this.idProduto = idProduto;
		this.totalQtd = totalQtd;
		this.sumValorTotal = sumValorTotal;
		this.valorMedio = valorMedio;
	}
	
}
