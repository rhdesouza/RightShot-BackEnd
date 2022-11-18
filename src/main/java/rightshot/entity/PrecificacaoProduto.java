package rightshot.entity;

import java.math.BigDecimal;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import lombok.Builder;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightshot.audit.Auditable;
import rightshot.dto.PrecificacaoProdutoListDTO;
import rightshot.vo.ValorMedioPorProdutoVO;

//@formatter:off
@NamedNativeQueries({ 
	@NamedNativeQuery(name = "PrecificacaoProdutoList", query =
			" SELECT " +
			" tp.id AS idProduto, " +
			" tp.cod_produto, " +
			" tp.descricao, " +
			" ttp.tipo, " +
			" ttp.unid_compra, " +
			" ttp.unid_venda, " +
			" tpp.id, " +
			" tpp.valor_produto, " +
			" er.qtd_est_real " +
			" FROM " +
			" tb_produto tp " +
			" LEFT JOIN " +
			" tb_tipo_produto ttp ON ttp.id = tp.tipo_produto_id " +
			" LEFT JOIN " +
			" tb_precificacao_produto tpp ON tpp.produto_id = tp.id " +
			" LEFT JOIN " +
			" (SELECT " +
			" estoque.*, " +
			" SUM(IFNULL(tvi.qtd_venda, 0)) AS qtd_venda, " +
			" (SUM(estoque.qtd_nota) - (SUM(IFNULL(tvi.qtd_venda, 0)))) AS qtd_est_real " +
			" FROM " +
			" (SELECT " +
			" tpor.id AS id_produto, " +
			" tpor.descricao, " +
			" SUM(tes.qtd_nota) AS qtd_nota, " +
			" SUM(tes.qtd_estoque) AS qtd_estoque, " +
			" tpor.estoque_minimo, " +
			" ttp.tipo, " +
			" ttp.unid_compra, " +
			" ttp.unid_venda " +
			" FROM " +
			" tb_estoque tes " +
			" JOIN tb_produto tpor ON tpor.id = tes.produto_id " +
			" JOIN tb_tipo_produto ttp ON ttp.id = tpor.tipo_produto_id " +
			" GROUP BY tpor.id) estoque " +
			" LEFT JOIN (SELECT " +
			" vi.produto_id AS id_produto, SUM(vi.qtd) AS qtd_venda " +
			" FROM " +
			" tb_venda_itens vi " +
			" JOIN tb_venda tv ON tv.id = vi.venda_id " +
			" WHERE tv.situacao_venda = 0 " +
			" GROUP BY vi.produto_id) tvi ON tvi.id_produto = estoque.id_produto " +
			" GROUP BY estoque.id_produto " +
			" ORDER BY descricao ASC) er ON er.id_produto = tp.id" +
		"HashWhereFilter ",
	resultSetMapping = "PrecificacaoProdutoListResultMapping"),
	
	@NamedNativeQuery(name = "PrecificacaoProdutoPorIdSugerido", query =
		" select " +
		" 	tni.produto_id as idProduto, " +
		" 	sum(tni.qtd) as totalQtd, " +
		"     sum(tni.valor_total) as sumValorTotal, " +
		"     sum(tni.valor_total) / sum(tni.qtd) as valorMedio " +
		" from  " +
		" 	tb_nf_itens tni " +
		" where  " +
		" 	tni.produto_id = :idProduto " +
		" group by  " +
		"     tni.produto_id " ,
	resultSetMapping = "PrecificacaoProdutoPorIdSugeridoResultMapping"),
	

})

@SqlResultSetMappings(
	{@SqlResultSetMapping(name = "PrecificacaoProdutoListResultMapping", 
		classes = {@ConstructorResult(targetClass = PrecificacaoProdutoListDTO.class, 
			columns = {
				@ColumnResult(name = "idProduto", type = Long.class),
				@ColumnResult(name = "cod_produto", type = String.class),
				@ColumnResult(name = "descricao", type = String.class),
				@ColumnResult(name = "tipo", type = String.class),
				@ColumnResult(name = "unid_compra", type = String.class),
				@ColumnResult(name = "unid_venda", type = String.class),
				@ColumnResult(name = "id", type = Long.class),
				@ColumnResult(name = "valor_produto", type = BigDecimal.class),
				@ColumnResult(name = "qtd_est_real", type = Long.class)
		})
	}),
		
	@SqlResultSetMapping(name = "PrecificacaoProdutoPorIdSugeridoResultMapping", 
		classes = {@ConstructorResult(targetClass = ValorMedioPorProdutoVO.class, 
			columns = {
				@ColumnResult(name = "idProduto", type = Long.class),
				@ColumnResult(name = "totalQtd", type = Long.class),
				@ColumnResult(name = "sumValorTotal", type = BigDecimal.class),
				@ColumnResult(name = "valorMedio", type = BigDecimal.class)
		})
	}),
	
})

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_precificacaoProduto")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@EntityListeners(AuditingEntityListener.class)
@Audited
public class PrecificacaoProduto extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Produto produto;
	private double markupReferncia;
	private BigDecimal valorMedioNF;
	private BigDecimal valorProdutoSugerido;
	private BigDecimal valorProduto;

	public PrecificacaoProduto(Produto produto, double markupReferncia, BigDecimal valorMedioNF,
			BigDecimal valorProdutoSugerido, BigDecimal valorProduto) {
		this.produto = produto;
		this.markupReferncia = markupReferncia;
		this.valorMedioNF = valorMedioNF;
		this.valorProdutoSugerido = valorProdutoSugerido;
		this.valorProduto = valorProduto;
	}
	
}
