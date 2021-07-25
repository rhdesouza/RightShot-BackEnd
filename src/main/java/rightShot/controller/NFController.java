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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rightShot.domain.Const;
import rightShot.entity.NF;
import rightShot.service.NFService;

@RestController
@RequestMapping("/nf")
public class NFController {

	@Autowired
	private NFService nfService;

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_NOTAFISCAL_NEW, Const.ROLE_NOTAFISCAL_EDIT })
	@PutMapping("/add")
	public ResponseEntity<NF> addNF(@RequestBody final NF nf) {
		return Optional.ofNullable(nfService.save(nf))
				.map(result -> new ResponseEntity<NF>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<NF>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_NOTAFISCAL })
	@GetMapping("/getAll")
	@Cacheable
	public ResponseEntity<List<NF>> getAllNFCadastradas() {
		return Optional.ofNullable(nfService.getAllNF())
				.map(result -> new ResponseEntity<List<NF>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<NF>>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_NOTAFISCAL_EDIT })
	@GetMapping("/one/{idNotaFiscal}")
	public ResponseEntity<NF> buscarNotaFiscalPorId(@PathVariable(name = "idNotaFiscal") Long idNotaFiscal) {
		return Optional.ofNullable(nfService.getOneNotaFiscal(idNotaFiscal))
				.map(result -> new ResponseEntity<NF>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<NF>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_ESTOQUE_NEW })
	@PostMapping("/add/estoque/{idNotaFiscal}")
	public ResponseEntity<NF> gerarEstoqueNF(@PathVariable(name = "idNotaFiscal") Long idNotaFiscal) {
		return Optional.ofNullable(nfService.gerarEstoqueNF(idNotaFiscal))
				.map(result -> new ResponseEntity<NF>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<NF>(HttpStatus.NOT_FOUND));
	}

}
