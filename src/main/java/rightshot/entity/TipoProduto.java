package rightshot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_tipo_produto")
public class TipoProduto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String tipo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UnidadeTipoProduto unidCompra;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UnidadeTipoProduto unidVenda;

	public TipoProduto(Long id) {
		super();
		this.id = id;
	}

	public TipoProduto(String tipo, UnidadeTipoProduto unidCompra, UnidadeTipoProduto unidVenda) {
		this.tipo = tipo;
		this.unidCompra = unidCompra;
		this.unidVenda = unidVenda;
	}
	
}
