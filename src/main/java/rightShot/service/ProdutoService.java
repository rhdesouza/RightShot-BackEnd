package rightShot.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import rightShot.entity.Ncm;
import rightShot.entity.Produto;
import rightShot.entity.TipoProduto;
import rightShot.repository.INcm;
import rightShot.repository.IProduto;
import rightShot.repository.ITipoProduto;

@Slf4j
@Service
public class ProdutoService {

	@Autowired
	private IProduto iProdutoDao;

	@Autowired
	private ITipoProduto iTipoProduto;

	@Autowired
	private INcm iNcm;

	public Produto save(Produto produto) {
		try {
			if (produto.getTipoProduto().getTipo() == null) {
				TipoProduto tp = iTipoProduto.findById(produto.getTipoProduto().getId())
						.orElseThrow(()-> new NotFoundException("Tipo de Produto não localizado"));
				produto.setTipoProduto(tp);
			}

			if (produto.getNcm().getCategoria() == null) {
				Ncm ncm = iNcm.findById(produto.getNcm().getNcm())
						.orElseThrow(()-> new NotFoundException("NCM não localizado"));
				produto.setNcm(ncm);
			}

			Produto prod = iProdutoDao.save(produto);

			return prod;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public List<Produto> getAllProdutos() {
		return iProdutoDao.findAll();
	}

	public Produto desativarProduto(Long idProduto) {
		try {
			Produto prod = iProdutoDao.findById(idProduto)
					.orElseThrow(()-> new NotFoundException("Produto não localizado."));
			
			prod.setDataDesativacao(Calendar.getInstance());
			
			return iProdutoDao.save(prod);
		} catch (Exception e) {
			log.error(e.toString());
			return null;
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
