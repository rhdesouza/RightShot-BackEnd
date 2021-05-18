package rightShot.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rightShot.domain.Const;
import rightShot.entity.Role;
import rightShot.entity.User;
import rightShot.repository.RoleRepository;
import rightShot.repository.UserRepository;
import rightShot.service.MyUserDetailsService;
import rightShot.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	MyUserDetailsService myUserDetailsService;

	@Autowired
	private RoleRepository iRole;

	@Autowired
	private UserService userService;

	@Secured({ Const.ROLE_ADMIN_ADMIN })
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<User> save(@RequestBody User user) {
		user = this.userRepository.save(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN })
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<User> edit(@RequestBody User user) {
		user = this.userRepository.save(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/* @Secured({ Const.ROLE_CLIENT, Const.ROLE_ADMIN }) */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Page<User>> list(@RequestParam("page") int page, @RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
		return new ResponseEntity<Page<User>>(userRepository.findAll(pageable), HttpStatus.OK);
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN, Const.ROLE_CLIENTE })
	@RequestMapping(value = ("/setFotoUsuario/{idUser}"), headers = "content-type=multipart/form-data", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@PathVariable(name = "idUser") Long idUser,
			@RequestParam("file") MultipartFile file) throws IOException {
		return myUserDetailsService.saveFotoUser(file, idUser);

	}

	@GetMapping("/one/{idUser}")
	public ResponseEntity<User> buscarUsuarioPorId(@PathVariable(name = "idUser") Long idUser) {
		return myUserDetailsService.buscarUsuarioPorId(idUser);
	}

	@GetMapping("/deleteImage/{idUser}")
	public ResponseEntity<User> apagarImageUser(@PathVariable(name = "idUser") Long idUser) {
		return myUserDetailsService.excluirImagemUser(idUser);
	}

	/* @Secured({ Const.ROLE_CLIENT, Const.ROLE_ADMIN }) */
	@GetMapping("/getAllUser")
	public ResponseEntity<List<User>> buscarTodosUsuarios() {
		return new ResponseEntity<List<User>>(userRepository.findAll(), HttpStatus.OK);
	}

	/* @Secured({ Const.ROLE_CLIENT, Const.ROLE_ADMIN }) */
	@GetMapping("/getAllRoles")
	public ResponseEntity<List<Role>> buscarTodasRoles2() {
		return new ResponseEntity<List<Role>>(iRole.findAll(), HttpStatus.OK);
	}


	/* @Secured({ Const.ROLE_CLIENT, Const.ROLE_ADMIN }) */
	@PutMapping("/roles/add")
	public ResponseEntity<Role> addTipoProduto(@RequestBody final Role role) {
		return userService.save(role);
	}

	@PostMapping("/disabled/{idUser}")
	public ResponseEntity<?> desativarUsuario(@PathVariable(name = "idUser") Long idUser) {
		return userService.desativarUser(idUser);
	}

	@PostMapping("/saveRoleUser")
	public ResponseEntity<?> salvarRoleUsuario(@RequestBody User user) {
		return userService.salvarUsuario(user);
	}

	@PostMapping("/saveNewUser")
	public ResponseEntity<?> salvarNewUsuario(@RequestBody User user) {
		return userService.salvarNovoUsuario(user);
	}

}
