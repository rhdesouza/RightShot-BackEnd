package rightShot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rightShot.documentation.DocumentationProdutoController;
import rightShot.domain.Const;
import rightShot.entity.Produto;
import rightShot.service.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoController implements DocumentationProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRODUTO_NEW, Const.ROLE_PRODUTO_EDIT })
	@PutMapping("/add")
	public ResponseEntity<Produto> addProduto(@RequestBody final Produto produto) {
		return Optional.ofNullable(produtoService.save(produto))
				.map(result -> new ResponseEntity<Produto>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Produto>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRODUTO })
	@GetMapping("/all")
	public ResponseEntity<List<Produto>> getAllProdutos() {
		return Optional.ofNullable(produtoService.getAllProdutos())
				.map(result -> new ResponseEntity<List<Produto>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<Produto>>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRODUTO_DISABLED })
	@PostMapping("/desativar/{idProduto}")
	public ResponseEntity<Produto> desativarProduto(@PathVariable(name = "idProduto") Long idProduto) {
		return Optional.ofNullable(produtoService.desativarProduto(idProduto))
				.map(result -> new ResponseEntity<Produto>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Produto>(HttpStatus.NOT_FOUND));

	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_PRODUTO })
	@GetMapping("/one/{idProduto}")
	public ResponseEntity<Produto> buscarProdutoEspecifico(@PathVariable(name = "idProduto") Long idProduto) {
		return Optional.ofNullable(produtoService.getOneProduto(idProduto))
				.map(result -> new ResponseEntity<Produto>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Produto>(HttpStatus.NOT_FOUND));
	}
}
