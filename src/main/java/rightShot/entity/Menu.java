package rightShot.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_menu")
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	private String descricao;
	@NotNull
	private String menu;
	@NotNull
	private String routerLink;
	private String icon;
	@NotNull
	private Integer ordem;
	@NotNull
	private Boolean disable;

	public Menu(String descricao, String menu, String routerLink, String icon, Integer ordem, Boolean disable) {
		this.descricao = descricao;
		this.menu = menu;
		this.routerLink = routerLink;
		this.icon = icon;
		this.ordem = ordem;
		this.disable = disable;
	}

}
