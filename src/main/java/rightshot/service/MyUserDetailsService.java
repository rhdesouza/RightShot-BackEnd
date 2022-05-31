package rightshot.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import rightshot.entity.SituacaoUser;
import rightshot.entity.User;
import rightshot.repository.UserRepository;

/**
 * @author Nataniel Paiva <nataniel.paiva@gmail.com>
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUser(username);
        if (user == null || user.getSituacao() != SituacaoUser.ATIVO) {
            throw new UsernameNotFoundException("Usuário {0} não existe!");
        }
        return new UserRepositoryUserDetails(user);
    }

    private final static class UserRepositoryUserDetails extends User implements UserDetails {

        private static final long serialVersionUID = 1L;

        private UserRepositoryUserDetails(User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return getRoles();
        }

        @Override
        public String getUsername() {
            return this.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getPassword() {
            return super.getPassword();
        }

    }

    public ResponseEntity<?> saveFotoUser(MultipartFile fotoUsuario, Long idUser) throws IOException {
        User user = userRepository.findById(idUser).orElseThrow(IOException::new);
        if (user.getData() != null)
            throw new MultipartException("Já existe uma foto para este Usuário!");

        try {
            String fileName = StringUtils.cleanPath(fotoUsuario.getOriginalFilename());
            if (fileName.contains("..")) {
                throw new MultipartException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            user.setFileName(fileName);
            user.setFileType(fotoUsuario.getContentType());
            user.setSize(fotoUsuario.getSize());
            user.setData(fotoUsuario.getBytes());
            User userSaved = userRepository.save(user);

            return new ResponseEntity<>(userSaved, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<User> buscarUsuarioPorId(Long idUser) {
        try {
            User user = userRepository.findById(idUser).orElse(null);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<User> excluirImagemUser(Long idUser) {
        try {
            User user = userRepository.findById(idUser).orElseThrow(Exception::new);
            user.setFileName(null);
            user.setFileType(null);
            user.setSize(null);
            user.setData(null);

            user = userRepository.save(user);

            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
