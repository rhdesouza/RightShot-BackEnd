package rightShot.entity;

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

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightShot.audit.Auditable;
import rightShot.dto.PrecificacaoProdutoListDTO;
import rightShot.vo.ValorMedioPorProdutoVO;

//@formatter:off
@NamedNativeQueries({ 
	@NamedNativeQuery(name = "PrecificacaoProdutoList", query =
		" select " +
	    " 	  tp.id as idProduto, " +
		" 	  tp.cod_produto, " +
		"     tp.descricao, " +
		"     ttp.tipo, " +
		"     ttp.unid_compra, " +
		"     ttp.unid_venda, " +
		"     tpp.id, " +
		"     tpp.valor_produto " +
		" from " +
		" 	tb_produto tp " +
		" left join " +
		" 	tb_tipo_produto ttp on ttp.id = tp.tipo_produto_id " +
		" left join " +
		" 	tb_precificacao_produto tpp on tpp.produto_id = tp.id " +
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
				@ColumnResult(name = "valor_produto", type = BigDecimal.class)
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
