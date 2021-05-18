package rightShot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rightShot.domain.Const;
import rightShot.entity.Role;
import rightShot.entity.SituacaoUser;
import rightShot.entity.User;
import rightShot.repository.RoleRepository;
import rightShot.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private RoleRepository iRole;

	@Autowired
	private UserRepository iUser;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	public ResponseEntity<Role> save(Role newRole) {
		try {
			if (iRole.getOneRoleByName(newRole.getName()) != null)
				return new ResponseEntity<>(null, HttpStatus.CONFLICT);

			Role role = iRole.save(newRole);

			return new ResponseEntity<Role>(role, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<?> desativarUser(Long idUser) {
		User user = iUser.findById(idUser).get();
		if (user.getUser().toUpperCase().equals("ADMIN"))
			return new ResponseEntity<String>("Usuário ADM não pode ser desativado.", HttpStatus.PRECONDITION_FAILED);
		user.setSituacao(SituacaoUser.INATIVO);
		User userDesativado = iUser.save(user);
		return new ResponseEntity<User>(userDesativado, HttpStatus.OK);
	}

	public ResponseEntity<?> salvarUsuario(User userFront) {
		List<Role> role = new ArrayList<Role>();
		role = userFront.getRoles();
		
		User user = iUser.findById(userFront.getId()).get();
		
		if (role.isEmpty())
			role = Arrays.asList(iRole.findByName(Const.ROLE_ADMIN_ADMIN));
		
		user.setRoles(null);
		user = iUser.save(user);

		user.setRoles(role);
		user = iUser.save(user);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	public ResponseEntity<?> salvarNovoUsuario(User userFront) {
		
		User user = new User(userFront.getNomeCompleto(), userFront.getUser(), userFront.getEmail(), 
				passwordEncoder.encode("12345"), null, SituacaoUser.ATIVO);
		
		return new ResponseEntity<User>(iUser.save(user), HttpStatus.OK);
	}

}
