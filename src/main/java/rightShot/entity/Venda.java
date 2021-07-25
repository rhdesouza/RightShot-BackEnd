package rightShot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import rightShot.audit.Auditable;
import rightShot.dto.ClienteDTO;
import rightShot.dto.VendaPageableDTO;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

//@formatter:off
@NamedNativeQueries({
		@NamedNativeQuery(name = "VendaPageableDTO", query =
				"SELECT * FROM ( " +
				" SELECT " +
				"	tv.id as idVenda, " +
				"	tv.data_hora_venda as dataHoraVenda, " +
				"	tv.email_enviado as emailEnviado, " +
				"	tc.cpf, " +
				" 	tc.nome, " +
				" 	tc.email, " +
				"   tv.situacao_venda as situacaoVenda" +
				" FROM tb_venda tv "+
				" JOIN tb_cliente tc on tc.id = tv.cliente_id " +
				") v " +
				" HashWhereFilter " +
				" HashWhereOrderBy ",
				resultSetMapping = "VendaPageableDTOResultMapping")
})

@SqlResultSetMapping(name = "VendaPageableDTOResultMapping", classes = {
		@ConstructorResult(targetClass = VendaPageableDTO.class,
				columns = {
						@ColumnResult(name = "idVenda", type = Long.class),
						@ColumnResult(name = "dataHoraVenda", type = Date.class),
						@ColumnResult(name = "emailEnviado", type = Boolean.class),
						@ColumnResult(name = "cpf", type = String.class),
						@ColumnResult(name = "nome", type = String.class),
						@ColumnResult(name = "email", type = String.class),
						@ColumnResult(name = "situacaoVenda", type = Integer.class)
				}
		)
})
//@formatter:on

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_venda")
@Audited
public class Venda extends Auditable<String> implements Serializable {

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
	@CreationTimestamp
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
	@ColumnDefault("FALSE")
	private Boolean emailEnviado;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private SituacaoVenda situacaoVenda = SituacaoVenda.CONSOLIDADO;

}
