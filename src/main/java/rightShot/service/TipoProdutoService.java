package rightShot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import rightShot.entity.TipoProduto;
import rightShot.repository.ITipoProduto;

@Slf4j
@Service
public class TipoProdutoService {

	@Autowired
	private ITipoProduto iTipoProdutoDao;

	public List<TipoProduto> getAllTipoProduto() {
		return iTipoProdutoDao.findAll();
	}

	public TipoProduto save(TipoProduto tipoProduto) {
		TipoProduto tProd;
		try {
			tProd = iTipoProdutoDao.save(tipoProduto);
			return tProd;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}

	}

	public TipoProduto getTipoProduto(Long idTipoProduto) {
		try {
			TipoProduto tProd = iTipoProdutoDao.findById(idTipoProduto)
					.orElseThrow(() -> new NotFoundException("Tipo produto não localizado"));
			return tProd;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}

	}
}
