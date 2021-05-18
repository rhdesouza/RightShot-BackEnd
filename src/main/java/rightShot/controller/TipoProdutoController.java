package rightShot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rightShot.domain.Const;
import rightShot.entity.TipoProduto;
import rightShot.service.TipoProdutoService;

@RestController
@RequestMapping("/tipoProduto")
public class TipoProdutoController {
	
	@Autowired
	TipoProdutoService tipoProdutoService;
	
	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_TIPOPRODUTO})
	@GetMapping("/all")
	@Cacheable
	public ResponseEntity<List<TipoProduto>> buscarAllTipoProduto() {
		return Optional.ofNullable(tipoProdutoService.getAllTipoProduto())
				.map(result -> new ResponseEntity<List<TipoProduto>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<TipoProduto>>(HttpStatus.NOT_FOUND));
	}
	
	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_TIPOPRODUTO_NEW})
	@PutMapping("/add")
	public ResponseEntity<TipoProduto> addTipoProduto(@RequestBody final TipoProduto tipoProduto){
		return Optional.ofNullable(tipoProdutoService.save(tipoProduto))
				.map(result-> new ResponseEntity<TipoProduto>(result,HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<TipoProduto>(HttpStatus.NOT_FOUND));
	}
	
	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_TIPOPRODUTO})
	@GetMapping("/one/{idTipoProduto}")
	public ResponseEntity<TipoProduto> buscarTipoProduto(@PathVariable(name = "idTipoProduto") Long idTipoProduto) {
		return Optional.ofNullable(tipoProdutoService.getTipoProduto(idTipoProduto))
				.map(result-> new ResponseEntity<TipoProduto>(result,HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<TipoProduto>(HttpStatus.NOT_FOUND));
	}
	
}
