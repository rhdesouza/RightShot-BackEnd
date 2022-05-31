package rightshot.entity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightshot.audit.Auditable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_nf")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@EntityListeners(AuditingEntityListener.class)
@Audited
public class NF extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@NotNull
	private Fornecedor fornecedor;

	@Column(nullable = false)
	@NotNull
	private Integer numero;

	@Column(nullable = false)
	@NotNull
	private BigDecimal valorTotal;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private FormaPagamento formaPagamento;

	private Integer parcelas;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nf_id")
	@NotAudited
	private List<NfItens> itens;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nf_id")
	@NotAudited
	private List<NfPagamento> pagamento;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private SituacaoNF situacao;

	public NF(Fornecedor fornecedor, Integer numero, BigDecimal valorTotal, FormaPagamento formaPagamento,
			Integer parcelas, List<NfItens> itens, List<NfPagamento> pagamento, SituacaoNF situacao) {
		super();
		this.fornecedor = fornecedor;
		this.numero = numero;
		this.valorTotal = valorTotal;
		this.formaPagamento = formaPagamento;
		this.parcelas = parcelas;
		this.itens = itens;
		this.pagamento = pagamento;
		this.situacao = situacao;
	}

	@Transient
	public String getValorTotalFormatado() {
		NumberFormat dinheiro = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		return dinheiro.format(getValorTotal());
	}

}
