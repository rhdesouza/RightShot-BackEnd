package rightShot.entity;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import rightShot.audit.Auditable;
import rightShot.dto.EstoqueDTO;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@formatter:off
@NamedNativeQueries({ 
	@NamedNativeQuery(name = "EstoqueSintetico", query =
			" SELECT " +
			" estoque.*, " +
			" SUM(IFNULL(tvi.qtd_venda, 0)) AS qtd_venda, " +
			" (SUM(estoque.qtd_nota) - (SUM(ifnull(tvi.qtd_venda,0)))) as qtd_est_real " +
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
			" LEFT JOIN " +
			" (SELECT " +
			" vi.produto_id AS id_produto, SUM(vi.qtd) AS qtd_venda " +
			" FROM " +
			" tb_venda_itens vi " +
			" JOIN tb_venda tv ON tv.id = vi.venda_id " +
			" WHERE tv.situacao_venda = 0 " +
			" GROUP BY vi.produto_id) tvi ON tvi.id_produto = estoque.id_produto " +
			" GROUP BY estoque.id_produto " ,
	resultSetMapping = "EstoqueSinteticoResultMapping") 
})

@SqlResultSetMapping(name = "EstoqueSinteticoResultMapping", classes = {
	@ConstructorResult(targetClass = EstoqueDTO.class, 
		columns = {
			@ColumnResult(name = "id_produto", type = Long.class),
			@ColumnResult(name = "descricao", type = String.class),
			@ColumnResult(name = "qtd_nota", type = Long.class),
			@ColumnResult(name = "qtd_estoque", type = Long.class),
			@ColumnResult(name = "estoque_minimo", type = Long.class),
			@ColumnResult(name = "tipo", type = String.class),
			@ColumnResult(name = "unid_compra", type = String.class),
			@ColumnResult(name = "unid_venda", type = String.class),
			@ColumnResult(name = "qtd_venda", type = Long.class),
			@ColumnResult(name = "qtd_est_real", type = Long.class),
		}
	) 
})
//@formatter:on
@AllArgsConstructor
@NoArgsConstructor 
@Data
@Entity
@Table(name = "tb_estoque")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Estoque extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotAudited
	@OneToOne(fetch = FetchType.EAGER)
	private NfItens nfItens;

	@NotAudited
	@OneToOne(fetch = FetchType.EAGER)
	private Produto produto;

	@NotNull
	private Long qtdNota;
	@NotNull
	private Long qtdEstoque;

	public Estoque(NfItens nfItens, Produto produto, Long qtdNota, Long qtdEstoque) {
		this.nfItens = nfItens;
		this.produto = produto;
		this.qtdNota = qtdNota;
		this.qtdEstoque = qtdEstoque;
	}

}
