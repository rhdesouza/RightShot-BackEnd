package rightShot.entity;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_nf_pagamento")
public class NfPagamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer parcela;

	@Column(nullable = false)
	private BigDecimal valorTotal;

	@DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
	private Calendar dataPagamento;

	public NfPagamento(Integer parcela, BigDecimal valorTotal, Calendar dataPagamento) {
		this.parcela = parcela;
		this.valorTotal = valorTotal;
		this.dataPagamento = dataPagamento;
	}

}
