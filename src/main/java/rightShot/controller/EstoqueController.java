package rightShot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rightShot.domain.Const;
import rightShot.dto.EstoqueDTO;
import rightShot.entity.Estoque;
import rightShot.service.EstoqueService;
import rightShot.vo.PageVO;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

	@Autowired
	EstoqueService estoqueService;

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_ESTOQUE })
	@GetMapping("/getAll")
	public ResponseEntity<List<Estoque>> bustaEstoque() {
		return Optional.ofNullable(estoqueService.getAllEstoque())
				.map(result -> new ResponseEntity<List<Estoque>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<Estoque>>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_ESTOQUE })
	@GetMapping("/getAllSintetico")
	public ResponseEntity<List<EstoqueDTO>> buscaEstoqueSintetico() {
		return Optional.ofNullable(estoqueService.getAllEstoqueSintetico())
				.map(result -> new ResponseEntity<List<EstoqueDTO>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<EstoqueDTO>>(HttpStatus.NOT_FOUND));

	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_ESTOQUE })
	@PostMapping("/getAllSinteticoPageable")
	public ResponseEntity<PageVO<EstoqueDTO>> buscaEstoqueSinteticoPageable(
			@RequestBody final PageVO<EstoqueDTO> pageVo) {
		return Optional.ofNullable(estoqueService.getAllEstoqueSintetivoPageable(pageVo))
				.map(result -> new ResponseEntity<PageVO<EstoqueDTO>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<PageVO<EstoqueDTO>>(HttpStatus.NOT_FOUND));
	}

}
