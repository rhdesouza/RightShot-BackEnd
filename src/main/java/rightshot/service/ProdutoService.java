package rightshot.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import rightshot.entity.Ncm;
import rightshot.entity.Produto;
import rightshot.entity.TipoProduto;
import rightshot.exception.RegraDeNegocioException;
import rightshot.repository.NcmRepository;
import rightshot.repository.ProdutoRepository;
import rightshot.repository.TipoProdutoRepository;

@Slf4j
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository iProdutoDao;

    @Autowired
    private TipoProdutoRepository iTipoProduto;

    @Autowired
    private NcmRepository iNcm;

    public Produto save(Produto produto) {
        try {
            if (produto.getTipoProduto().getTipo() == null) {
                TipoProduto tp = iTipoProduto.findById(produto.getTipoProduto().getId())
                        .orElseThrow(() -> new RegraDeNegocioException("Tipo Produto não localizado"));

                produto.setTipoProduto(tp);
            }

            if (produto.getNcm().getCategoria() == null) {
                Ncm ncm = iNcm.findById(produto.getNcm().getNcm())
                        .orElseThrow(() -> new RegraDeNegocioException("NCM não localizado"));
                produto.setNcm(ncm);
            }

            Produto prod = iProdutoDao.save(produto);

            return prod;
        } catch (Exception e) {
            log.error(e.toString());
            throw new RegraDeNegocioException(e.getMessage());
        }
    }

    public List<Produto> getAllProdutos() {
        return iProdutoDao.findAll();
    }

    public Produto desativarProduto(Long idProduto) {
        try {
            Produto prod = iProdutoDao.findById(idProduto)
                    .orElseThrow(() -> new NotFoundException("Produto não localizado."));

            prod.setDataDesativacao(Calendar.getInstance());

            return iProdutoDao.save(prod);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RegraDeNegocioException(e.getMessage());
        }
    }

    public Produto getOneProduto(Long idProduto) {
        try {
            Produto prod = iProdutoDao.buscarProdutoPorId(idProduto);
            return prod;
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

}
