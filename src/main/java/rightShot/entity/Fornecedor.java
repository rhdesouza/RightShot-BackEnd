package rightShot.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rightShot.audit.Auditable;
import rightShot.dto.FornecedorDTO;

//@formatter:off
@NamedNativeQueries({ 
	@NamedNativeQuery(name = "FornecedorDTO", query =
			" SELECT " +
			"     tfor.id, " +
			"     tfor.razao_social, " +
			"     tfor.nome_fantasia, " +
			"     tfor.cpf_cnpj, " +
			"     tfor.telefone, " +
			"     tfor.situacao " +
			" FROM " +
			"     tb_fornecedor tfor " + 
			" HashWhereFilter " +
			" HashWhereOrderBy ",
	resultSetMapping = "FornecedorResultMapping") 
})

@SqlResultSetMapping(name = "FornecedorResultMapping", classes = {
	@ConstructorResult(targetClass = FornecedorDTO.class, 
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "razao_social", type = String.class),
			@ColumnResult(name = "nome_fantasia", type = String.class),
			@ColumnResult(name = "cpf_cnpj", type = String.class),
			@ColumnResult(name = "telefone", type = String.class),
			@ColumnResult(name = "situacao", type = String.class),
		}
	) 
})


@Entity
@Table(name = "tb_fornecedor")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Fornecedor extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String razaoSocial;
	private String nomeFantasia;
	/* Contato Endereco */
	private String cep;
	private String logradouro;
	private String numero;
	private String cidade;
	private String estado;
	private String pais;
	/* Contato Documentos */
	@NotNull
	private String cpfCnpj;
	private String inscricaoMunicipal;
	private String inscricaoEstadual;
	private String iss;
	/* Contato Fornecedor */
	private String email;
	@Column(nullable = false)
	private String telefone;
	private String telefone2;
	private String telefone3;
	/* Informações bancárias fornecedor */
	private String codBanco;
	private String agencia;
	private String conta;

	/* Contato no fornecedor */
	private String contatoNome;
	private String contatoCargo;
	private String contatoEmail;
	private String contatoTelefone;
	private String contatoTelefone2;
	private String contatoTelefone3;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SituacaoFornecedor situacao = SituacaoFornecedor.Ativo;

	public Fornecedor(Long id) {
		super();
		this.id = id;
	}

	public Fornecedor(Long id, String razaoSocial) {
		super();
		this.id = id;
		this.razaoSocial = razaoSocial;
	}

	/**
	 * Construtor somente de campos obrigatórios
	 * 
	 * @param razaoSocial
	 * @param nomeFantasia
	 */
	public Fornecedor(String razaoSocial, String nomeFantasia, String cpfCnpj, String telefone,
			SituacaoFornecedor situacao) {
		super();
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.cpfCnpj = cpfCnpj;
		this.telefone = telefone;
		this.situacao = situacao;
	}

	public Fornecedor(String razaoSocial, String nomeFantasia, String cep, String logradouro, String numero,
			String cidade, String estado, String pais, String cpfCnpj, String inscricaoMunicipal,
			String inscricaoEstadual, String iss, String email, String telefone, String telefone2, String telefone3,
			String codBanco, String agencia, String conta, String contatoNome, String contatoCargo, String contatoEmail,
			String contatoTelefone, String contatoTelefone2, String contatoTelefone3, SituacaoFornecedor situacao) {
		super();
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.cep = cep;
		this.logradouro = logradouro;
		this.numero = numero;
		this.cidade = cidade;
		this.estado = estado;
		this.pais = pais;
		this.cpfCnpj = cpfCnpj;
		this.inscricaoMunicipal = inscricaoMunicipal;
		this.inscricaoEstadual = inscricaoEstadual;
		this.iss = iss;
		this.email = email;
		this.telefone = telefone;
		this.telefone2 = telefone2;
		this.telefone3 = telefone3;
		this.codBanco = codBanco;
		this.agencia = agencia;
		this.conta = conta;
		this.contatoNome = contatoNome;
		this.contatoCargo = contatoCargo;
		this.contatoEmail = contatoEmail;
		this.contatoTelefone = contatoTelefone;
		this.contatoTelefone2 = contatoTelefone2;
		this.contatoTelefone3 = contatoTelefone3;
		this.situacao = situacao;
	}
	
	@Transient
	public String getCpfCnpjFormatado() {
		String retorno = null;
		
		if(getCpfCnpj() == null)
			return null;
		
		if (getCpfCnpj().length() == 11)
			retorno = getCpfCnpj().replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4");
		else
			retorno = getCpfCnpj().replaceAll("([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})", "$1.$2.$3/$4-$5");
		
		return retorno;
	}

}
