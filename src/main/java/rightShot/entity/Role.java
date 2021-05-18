package rightShot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;
import rightShot.audit.Auditable;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Audited
public class Role extends Auditable<String> implements GrantedAuthority{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Modulo modulo;

	@Enumerated(EnumType.STRING)
	private ModuloAcao acao;

	public Role(String name, Modulo modulo, ModuloAcao acao) {
		super();
		this.name = name;
		this.modulo = modulo;
		this.acao = acao;
	}
	
	public Role(String name, Modulo modulo) {
		super();
		this.name = name;
		this.modulo = modulo;
	}

	public Role() {
		super();
	}

	@Override
	public String getAuthority() {
		return this.name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}


	public ModuloAcao getAcao() {
		return acao;
	}

	public void setAcao(ModuloAcao acao) {
		this.acao = acao;
	}

}
