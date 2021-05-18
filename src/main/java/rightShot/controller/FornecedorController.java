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
import rightShot.dto.FornecedorDTO;
import rightShot.entity.Fornecedor;
import rightShot.service.FornecedorService;
import rightShot.vo.PageVO;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

	@Autowired
	FornecedorService fornecedorService;

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR })
	@GetMapping("/all")
	@Cacheable
	public ResponseEntity<List<Fornecedor>> buscarTodosFornecedores() {
		return Optional.ofNullable(fornecedorService.buscarTodosFornecedores())
				.map(result -> new ResponseEntity<List<Fornecedor>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<Fornecedor>>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR })
	@PostMapping("/getAllFornecedorPageable")
	public ResponseEntity<PageVO<FornecedorDTO>> getAllFornecedorPageable(@RequestBody final PageVO<FornecedorDTO> pageVo) {
		return Optional.ofNullable(fornecedorService.getAllFornecedorPageable(pageVo))
				.map(result -> new ResponseEntity<PageVO<FornecedorDTO>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<PageVO<FornecedorDTO>>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR })
	@GetMapping("/one/{idFornecedor}")
	public ResponseEntity<Fornecedor> buscarAirsoftPorId(@PathVariable(name = "idFornecedor") Long idFornecedor) {
		return Optional.ofNullable(fornecedorService.buscarFornecedorPorId(idFornecedor))
				.map(result -> new ResponseEntity<Fornecedor>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Fornecedor>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR_NEW, Const.ROLE_FORNECEDOR_EDIT })
	@PutMapping("/add")
	public ResponseEntity<Fornecedor> addFornecedor(@RequestBody final Fornecedor fornecedor){
		return Optional.ofNullable(fornecedorService.save(fornecedor))
				.map(result -> new ResponseEntity<Fornecedor>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Fornecedor>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR })
	@GetMapping("/allFornecedorRazaoSocial")
	@Cacheable
	public ResponseEntity<List<String>> getAllRazaoSocial() {
		return Optional.ofNullable(fornecedorService.buscarTodosFornecedoresRazaoSocial())
				.map(result -> new ResponseEntity<List<String>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<String>>(HttpStatus.NOT_FOUND));
	}

}
