package rightshot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rightshot.audit.Auditable;
import rightshot.converters.LocalDateConverter;
import rightshot.dto.ClienteDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

//@formatter:off
@NamedNativeQueries({ 
	@NamedNativeQuery(name = "ClienteDTO", query =
			"select "+
			"	tbcli.id, "+
			"   tbcli.nome, "+
			"   tbcli.email, "+
			"   tbcli.telefone, "+
			"   tbcli.situacao, "+
			"   tbcli.created_date "+
			"from "+
			"	tb_cliente tbcli " + 
			" HashWhereFilter " +
			" HashWhereOrderBy ",
	resultSetMapping = "ClienteDTOResultMapping") 
})

@SqlResultSetMapping(name = "ClienteDTOResultMapping", classes = {
	@ConstructorResult(targetClass = ClienteDTO.class, 
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nome", type = String.class),
			@ColumnResult(name = "email", type = String.class),
			@ColumnResult(name = "telefone", type = String.class),
			@ColumnResult(name = "situacao", type = String.class),
			@ColumnResult(name = "created_date", type = Date.class),
		}
	) 
})
//@formatter:on

@Entity
@Table(name = "tb_cliente")
@EntityListeners(AuditingEntityListener.class)
@Audited
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cliente extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	@Column(unique = true)
	private String cpf;
	private String email;

	@Convert(converter = LocalDateConverter.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;

	private String telefone;
	private String telefone2;

	private String cep;
	private String logradouro;
	private String numero;
	private String cidade;
	private String estado;
	private String pais;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SituacaoCliente situacao = SituacaoCliente.Ativo;

	public Cliente(Long id) {
		this.id = id;
	}

	public Cliente(Long id, String nome, String cpf) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
	}
	
}
