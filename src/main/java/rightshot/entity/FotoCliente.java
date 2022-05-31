package rightshot.entity;

import java.util.Calendar;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_fotoCliente")
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoCliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cliente cliente;

	private String fileName;
	private String fileType;
	private Long size;

	@DateTimeFormat
	private Calendar dataCadastro;

	@Lob
	private byte[] data;

	public FotoCliente(Cliente cliente, String fileName, String fileType, Long size, Calendar dataCadastro,
			byte[] data) {
		super();
		this.cliente = cliente;
		this.fileName = fileName;
		this.fileType = fileType;
		this.size = size;
		this.dataCadastro = dataCadastro;
		this.data = data;
	}

	public FotoCliente(String fileName, String fileType, byte[] data) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
	}

}
