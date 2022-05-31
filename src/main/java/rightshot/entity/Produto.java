package rightshot.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import rightshot.audit.Auditable;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_produto")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Produto extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private String codProduto;

	@NotNull
	@Column(nullable = false)
	private String descricao;

	@OneToOne(fetch = FetchType.EAGER)
	@NotNull
	@NotAudited
	private TipoProduto tipoProduto;

	@NotNull
	private Long estoqueMinimo;

	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@NotAudited
	private Ncm ncm;

	@DateTimeFormat
	private Calendar dataDesativacao;

	public Produto(Long id) {
		this.id = id;
	}

	public Produto(String codProduto, String descricao, TipoProduto tipoProduto, Long estoqueMinimo, Ncm ncm) {
		this.codProduto = codProduto;
		this.descricao = descricao;
		this.tipoProduto = tipoProduto;
		this.estoqueMinimo = estoqueMinimo;
		this.ncm = ncm;
	}

	public Produto(String codProduto, String descricao, TipoProduto tipoProduto, Long estoqueMinimo, Ncm ncm,
			Calendar dataDesativacao) {
		this.codProduto = codProduto;
		this.descricao = descricao;
		this.tipoProduto = tipoProduto;
		this.estoqueMinimo = estoqueMinimo;
		this.ncm = ncm;
		this.dataDesativacao = dataDesativacao;
	}


}
