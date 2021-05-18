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
		"select " +
		"tpor.id as id_produto, "+
		"tpor.descricao, " +
		"sum(tes.qtd_nota) as qtd_nota, " +
		"sum(tes.qtd_estoque) as qtd_estoque, " +
		"tpor.estoque_minimo, " +
		"ttp.tipo, " +
		"ttp.unid_compra, " +
		"ttp.unid_venda " +
		"from " +
		"tb_estoque tes " +
		"join tb_produto tpor on tpor.id = tes.produto_id " +
		"join tb_tipo_produto ttp on ttp.id = tpor.tipo_produto_id " +
		"HashWhereFilter " +
		"group by tpor.id " ,
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
			@ColumnResult(name = "unid_venda", type = String.class)
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
