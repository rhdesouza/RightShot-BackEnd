package rightshot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rightshot.entity.Estoque;
import rightshot.entity.NF;
import rightshot.entity.SituacaoNF;
import rightshot.exception.RegraDeNegocioException;
import rightshot.repository.EstoqueRepository;
import rightshot.repository.INFRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NFService {
    @Autowired
    INFRepository iNF;

    @Autowired
    EmailService emailService;

    @Autowired
    EstoqueRepository iEstoque;

    public NF save(NF nf) throws Exception {
        NF nota = new NF();
        try {
            if (nf.getSituacao().name().equals("Estoque"))
                throw new RegraDeNegocioException("Nota fiscal ja em estoque.");

            if (nf.getId() == null) {
                nota = iNF.save(nf);
                emailService.enviaNotaFiscal(nota);
            } else {
                nota = iNF.save(nf);
            }

            return nota;

        } catch (Exception e) {
            log.error(e.toString());
            throw new RegraDeNegocioException(e.getMessage());
        }
    }

    public List<NF> getAllNF() {
        return iNF.findAll();
    }

    public NF getOneNotaFiscal(Long idNotaFiscal) {
        try {
            NF nota = iNF.findById(idNotaFiscal)
                    .orElseThrow(() -> {
                        throw new RegraDeNegocioException("Nota fiscal não localizada.");
                    });
            return nota;
        } catch (Exception e) {
            log.error(e.toString());
            throw new RegraDeNegocioException(e.getMessage());
        }

    }

    public NF gerarEstoqueNF(Long idNotaFiscal) {
        try {
            NF nota = iNF.findById(idNotaFiscal)
                    .filter(x -> x.getSituacao().equals(SituacaoNF.Aberta))
                    .orElseThrow(() -> {
                        throw new RegraDeNegocioException("Nota fiscal não localiada, impossivel gerar estoque.");
                    });

            List<Estoque> estoque = new ArrayList<>();
            nota.getItens().stream().forEach(item -> estoque.add(new Estoque(item, item.getProduto(), item.getQtd(), item.getQtd())));

            iEstoque.saveAll(estoque);
            nota.setSituacao(SituacaoNF.Estoque);
            nota = iNF.save(nota);
            emailService.notificacaoGerarEstoque(nota);

            return nota;
        } catch (Exception e) {
            log.error(e.toString());
            throw new RegraDeNegocioException(e.getMessage());
        }

    }

}
