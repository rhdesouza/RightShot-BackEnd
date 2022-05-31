package rightshot.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightshot.audit.Auditable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_inforsc")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@EntityListeners(AuditingEntityListener.class)
@Audited
public class InfoRSC extends Auditable<String>{
	
	@Id
	private Integer id;
	
	@NotNull
	private String cnpj;
	@NotNull
	private String nomeEmpresa;
	@NotNull
	private String nomeFantasia;
	
	private String logradouro;
	private int numero;
	private String cep;
	private String complemento;
	private String bairro;
	private String municipio;
	private String uf;

	@NotNull
	private String telefone1;	
	private String telefone2;
	
	@NotNull
	private String emailEmpresa;
	
	@NotNull
	private String socio1;
	@NotNull
	private String emailSocio1;
	
	private String socio2;
	private String emailSocio2;
	
	private Double markup;
	
	
}
