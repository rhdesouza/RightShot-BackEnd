package rightShot.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_venda_itens")
@Audited
public class VendaItens {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Produto produto;
	
	@NotNull
	private String tipoUnidade;
	
	@NotNull
	private float qtd;
	
	@NotNull
	private BigDecimal valorProduto;
	
	@NotNull
	private BigDecimal valorDesconto;
	
	@NotNull
	private BigDecimal valorVenda;

}
