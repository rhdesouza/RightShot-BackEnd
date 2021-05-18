package rightShot.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightShot.audit.Auditable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_venda")
@Audited
public class Venda extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="cliente_id",referencedColumnName="id")
	private Cliente cliente;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FormaPagamento formaPagamento;

	@Column(nullable = false)
	private LocalDateTime dataHoraVenda;

	@Column(nullable = false)
	private BigDecimal valorTotalItens;
	
	@Column(nullable = false)
	private BigDecimal valorDescontoNota;
	
	@Column(nullable = false)
	private BigDecimal valorTotalVenda;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "venda_id")
	@NotAudited
	private List<VendaItens> vendaItens;

	@Column(nullable = false)
	private Boolean emailEnviado;

}
