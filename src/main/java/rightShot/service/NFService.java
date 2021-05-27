package rightShot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import rightShot.entity.Estoque;
import rightShot.entity.NF;
import rightShot.entity.SituacaoNF;
import rightShot.repository.EstoqueRepository;
import rightShot.repository.INFRepository;
import rightShot.repository.NfItensRepository;

@Slf4j
@Service
public class NFService {

	@Autowired
	private INFRepository iNF;

	@Autowired
	NfItensRepository iNfItens;

	@Autowired
	EmailService emailService;

	@Autowired
	EstoqueRepository iEstoque;

	public NF save(NF nf) {
		NF nota = new NF();
		try {
			if (nf.getSituacao().name().equals("Estoque"))
				throw new Exception("Nota fiscal ja em estoque.");

			if (nf.getId() == null) {
				nota = iNF.save(nf);
				emailService.enviaNotaFiscal(nota);
			} else {
				nota = iNF.save(nf);
			}

			return nota;

		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public List<NF> getAllNF() {
		return iNF.findAll();
	}

	public NF getOneNotaFiscal(Long idNotaFiscal) {
		try {
			NF nota = iNF.findById(idNotaFiscal)
					.orElseThrow(() -> new NotFoundException("Nota fiscal não localizada."));
			return nota;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}

	}

	public NF gerarEstoqueNF(Long idNotaFiscal) {
		try {
			NF nota = iNF.findById(idNotaFiscal)
					.filter(x-> x.getSituacao().equals(SituacaoNF.Aberta))
					.orElseThrow(()-> new NotFoundException("Nota fiscal não localiada, impossivel gear estoque."));

			List<Estoque> estoque = new ArrayList<>();
			nota.getItens().stream().forEach(item -> estoque.add(new Estoque(item, item.getProduto(), item.getQtd(), item.getQtd())));

			iEstoque.saveAll(estoque);
			nota.setSituacao(SituacaoNF.Estoque);
			nota = iNF.save(nota);
			emailService.notificacaoGerarEstoque(nota);

			return nota;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}

	}

}
