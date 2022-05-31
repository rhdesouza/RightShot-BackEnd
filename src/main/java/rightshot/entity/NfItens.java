package rightshot.entity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_nf_itens")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class NfItens {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Produto produto;

	@Column(nullable = false)
	private String codigoProduto;

	@Column(nullable = false)
	private String descricaoProduto;

	@Column(nullable = false)
	private String ncmSh;

	@Column(nullable = false)
	private String cst;

	@Column(nullable = false)
	private String cfop;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UnidadeNF un;

	@Column(nullable = false)
	private Long qtd;

	@Column(nullable = false)
	private BigDecimal valorUnit;

	@Column(nullable = false)
	private BigDecimal valorTotal;

	@Column(nullable = false)
	private BigDecimal aliquitaIcms;

	@Column(nullable = false)
	private BigDecimal aliquitaIpi;

	public NfItens(Produto produto, String codigoProduto, String descricaoProduto, String ncmSh, String cst,
			String cfop, UnidadeNF un, Long qtd, BigDecimal valorUnit, BigDecimal valorTotal, BigDecimal aliquitaIcms,
			BigDecimal aliquitaIpi) {

		this.produto = produto;
		this.codigoProduto = codigoProduto;
		this.descricaoProduto = descricaoProduto;
		this.ncmSh = ncmSh;
		this.cst = cst;
		this.cfop = cfop;
		this.un = un;
		this.qtd = qtd;
		this.valorUnit = valorUnit;
		this.valorTotal = valorTotal;
		this.aliquitaIcms = aliquitaIcms;
		this.aliquitaIpi = aliquitaIpi;
	}

	@Transient
	public String getvUnitFormatado() {
		NumberFormat dinheiro = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		return dinheiro.format(getValorUnit());
	}

	@Transient
	public String getvTotalFormatado() {
		NumberFormat dinheiro = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		return dinheiro.format(getValorTotal());
	}

}
