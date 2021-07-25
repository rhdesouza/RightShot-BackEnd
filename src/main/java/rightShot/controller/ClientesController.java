package rightShot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rightShot.documentation.DocumentationClienteController;
import rightShot.domain.Const;
import rightShot.dto.ClienteDTO;
import rightShot.entity.Cliente;
import rightShot.entity.FotoCliente;
import rightShot.service.ClientesService;
import rightShot.vo.PageVO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClientesController implements DocumentationClienteController {

	@Autowired
	ClientesService clienteService;

	@Operation(summary = "Retorna uma lista de Clientes")
	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE })
	@GetMapping("/all")
	@Cacheable
	public List<Cliente> buscarTodosClientes() {
		return clienteService.buscarTodosClientes();
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE })
	@PostMapping("/getAllClientePageable")
	public ResponseEntity<PageVO<ClienteDTO>> getAllClientePageable(@RequestBody final PageVO<ClienteDTO> pageVo) {
		try {
			return new ResponseEntity<PageVO<ClienteDTO>>(clienteService.getAllClientePageable(pageVo), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@GetMapping("/one/{idCliente}")
	public ResponseEntity<Cliente> buscarClientePorId(
			@Parameter(description = "Id do cliente")
			@PathVariable(name = "idCliente") Long idCliente
	) {
		return Optional.ofNullable(clienteService.buscarClientePorId(idCliente))
				.map(result -> new ResponseEntity<Cliente>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Cliente>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@PostMapping("/add")
	public ResponseEntity<Cliente> saveCliente(@RequestBody final Cliente cliente) throws Exception {
		return Optional.ofNullable(clienteService.saveCliente(cliente))
				.map(result -> new ResponseEntity<Cliente>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Cliente>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@PostMapping(value = ("/setFotoCliente/{idCliente}"), headers = "content-type=multipart/form-data")
	public ResponseEntity<FotoCliente> uploadFile(@PathVariable(name = "idCliente") Long idCliente,
			@RequestParam("file") MultipartFile file) {
		
		return Optional.ofNullable(clienteService.prepareSaveFoto(file, idCliente))
				.map(result -> new ResponseEntity<FotoCliente>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<FotoCliente>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@GetMapping("/getAllFotosCliente/{idCliente}")
	public ResponseEntity<List<FotoCliente>> getAllFotosCliente(@PathVariable(name = "idCliente") Long idCliente) {
		return Optional.ofNullable(clienteService.buscarTodasFotosPorCliente(idCliente))
				.map(result -> new ResponseEntity<List<FotoCliente>>(result, HttpStatus.OK))
				.orElseGet(()->new ResponseEntity<List<FotoCliente>>(HttpStatus.OK));
		
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@GetMapping(path = { "/getFotoCliente/{idFotoCliente}" })
	public ResponseEntity<FotoCliente> getImage(@PathVariable("idFotoCliente") Long idFotoCliente) {
		return Optional.ofNullable(clienteService.getFotoCliente(idFotoCliente))
				.map(result -> new ResponseEntity<FotoCliente>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<FotoCliente>(HttpStatus.NOT_FOUND));
		
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@DeleteMapping(path = { "/deleteFotoCliente/{idFotoCliente}" })
	public void DeleteFotoCliente(@PathVariable("idFotoCliente") Long idFotoCliente) throws Exception {
		clienteService.deleteFotoCliente(idFotoCliente);
	}

}
