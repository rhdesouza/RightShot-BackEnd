package rightShot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rightShot.domain.Const;
import rightShot.dto.PrecificacaoProdutoDTO;
import rightShot.dto.PrecificacaoProdutoHistoricoDTO;
import rightShot.dto.PrecificacaoProdutoListDTO;
import rightShot.entity.PrecificacaoProduto;
import rightShot.service.PrecificacaoProdutoService;
import rightShot.vo.PageVO;

@RestController
@RequestMapping("/precificacao")
public class PrecificacaoProdutoController {

	@Autowired
	private PrecificacaoProdutoService precificacaoProdutoService;

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRECIFICACAO })
	@PostMapping("/precificacaoProdutoList")
	public ResponseEntity<PageVO<PrecificacaoProdutoListDTO>> buscaPrecificacaoProduto(
			@RequestBody final PageVO<PrecificacaoProdutoListDTO> pageVo) {

		return Optional.ofNullable(precificacaoProdutoService.getAllPrecificacaoProduto(pageVo))
				.map(result -> new ResponseEntity<PageVO<PrecificacaoProdutoListDTO>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<PageVO<PrecificacaoProdutoListDTO>>(HttpStatus.NOT_FOUND));

	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRECIFICACAO_NEW, Const.ROLE_PRECIFICACAO_EDIT })
	@GetMapping("/precificacaoProduto/{idProduto}")
	public ResponseEntity<PrecificacaoProdutoDTO> buscarPrecificacaoPorIdProduto(
			@PathVariable(name = "idProduto") Long idProduto) {

		return Optional.ofNullable(precificacaoProdutoService.buscarPrecificacaoPorIdProduto(idProduto))
				.map(result -> new ResponseEntity<PrecificacaoProdutoDTO>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<PrecificacaoProdutoDTO>(HttpStatus.NOT_FOUND));

	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRECIFICACAO_NEW, Const.ROLE_PRECIFICACAO_EDIT })
	@PostMapping("/save")
	public ResponseEntity<PrecificacaoProduto> salvarPrecificacaoProduto(@RequestBody final PrecificacaoProduto pp) {

		return Optional.ofNullable(precificacaoProdutoService.salvarPrecificacao(pp))
				.map(result -> new ResponseEntity<PrecificacaoProduto>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<PrecificacaoProduto>(HttpStatus.NOT_FOUND));

	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRECIFICACAO_DELETE })
	@PostMapping("/deletePrecificacao/{idPrecificacao}")
	public ResponseEntity<PrecificacaoProduto> limparPrecificacaoProduto(
			@PathVariable(name = "idPrecificacao") Long idPrecificacao) {

		return Optional.ofNullable(precificacaoProdutoService.limparPrecificacaoProduto(idPrecificacao))
				.map(result -> new ResponseEntity<PrecificacaoProduto>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<PrecificacaoProduto>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRECIFICACAO_DELETE })
	@GetMapping("/historicoPrecificacaoProduto/{idProduto}")
	public ResponseEntity<PrecificacaoProdutoHistoricoDTO> getHistoricoPrecificacaoProduto(
			@PathVariable(name = "idProduto") Long idProduto) {

		return Optional.ofNullable(precificacaoProdutoService.getHistoricoPrecificacaoProduto(idProduto))
				.map(result -> new ResponseEntity<PrecificacaoProdutoHistoricoDTO>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<PrecificacaoProdutoHistoricoDTO>(HttpStatus.NOT_FOUND));
	}

}
