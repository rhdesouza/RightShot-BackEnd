package rightshot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_ncm")
public class Ncm implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 15)
	private String ncm;
	
	private String categoria;
	private String descricao;
	private String ipi;
	private String inicioVigencia;
	private String fimVigencia;
	private String uTrib;
	private String descricaoUTrib;
	private String gtinProducao;
	private String gtinHomologacao;
	private String observacao;

	public Ncm(String ncm) {
		this.ncm = ncm;
	}

	public Ncm(String ncm, String categoria, String descricao, String ipi, String inicioVigencia, String fimVigencia,
			String uTrib, String descricaoUTrib, String gtinProducao, String gtinHomologacao) {
		this.ncm = ncm;
		this.categoria = categoria;
		this.descricao = descricao;
		this.ipi = ipi;
		this.inicioVigencia = inicioVigencia;
		this.fimVigencia = fimVigencia;
		this.uTrib = uTrib;
		this.descricaoUTrib = descricaoUTrib;
		this.gtinProducao = gtinProducao;
		this.gtinHomologacao = gtinHomologacao;
	}
	
}
