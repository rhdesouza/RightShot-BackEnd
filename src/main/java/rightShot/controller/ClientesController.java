package rightShot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import rightShot.domain.Const;
import rightShot.dto.ClienteDTO;
import rightShot.entity.Cliente;
import rightShot.entity.FotoCliente;
import rightShot.service.ClientesService;
import rightShot.vo.PageVO;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

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
	public ResponseEntity<Cliente> buscarClientePorId(@PathVariable(name = "idCliente") Long idCliente) {
		return Optional.ofNullable(clienteService.buscarClientePorId(idCliente))
				.map(result -> new ResponseEntity<Cliente>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Cliente>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@PostMapping("/add")
	public ResponseEntity<Cliente> saveCliente(@RequestBody final Cliente cliente) {
		return Optional.ofNullable(clienteService.save(cliente))
				.map(result -> new ResponseEntity<Cliente>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Cliente>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	// @RequestMapping(value = ("/setFotoCliente/{idCliente}"), headers =
	// "content-type=multipart/form-data", method = RequestMethod.POST)
	@PostMapping(value = ("/setFotoCliente/{idCliente}"), headers = "content-type=multipart/form-data")
	public ResponseEntity<?> uploadFile(@PathVariable(name = "idCliente") Long idCliente,
			@RequestParam("file") MultipartFile file) {
		return clienteService.saveFoto(file, idCliente);

	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@GetMapping("/getAllFotosCliente/{idCliente}")
	public List<FotoCliente> getAllFotosCliente(@PathVariable(name = "idCliente") Long idCliente) {
		return clienteService.buscarTodasFotosPorCliente(idCliente);
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@GetMapping(path = { "/getFotoCliente/{idFotoCliente}" })
	public ResponseEntity<FotoCliente> getImage(@PathVariable("idFotoCliente") Long idFotoCliente) {
		return clienteService.getFotoCliente(idFotoCliente);
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE_EDIT, Const.ROLE_CLIENTE_NEW })
	@DeleteMapping(path = { "/deleteFotoCliente/{idFotoCliente}" })
	public ResponseEntity<?> DeleteFotoCliente(@PathVariable("idFotoCliente") Long idFotoCliente) {
		return clienteService.deleteFotoCliente(idFotoCliente);
	}

}
