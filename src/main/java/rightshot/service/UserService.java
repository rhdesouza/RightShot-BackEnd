package rightshot.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rightshot.domain.Const;
import rightshot.dto.TrocaSenhaUserDTO;
import rightshot.entity.Role;
import rightshot.entity.SituacaoUser;
import rightshot.entity.User;
import rightshot.exception.RegraDeNegocioException;
import rightshot.repository.RoleRepository;
import rightshot.repository.UserRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private RoleRepository iRole;

    @Autowired
    private UserRepository iUser;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    CryptoService crypto;

    @Value("${api.url.trocaSenha}")
    String urlTrocaSenha;

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
        User user = iUser.findById(idUser).orElseThrow();
        if (user.getUser().equalsIgnoreCase("ADMIN"))
            return new ResponseEntity<String>("Usuário ADM não pode ser desativado.", HttpStatus.PRECONDITION_FAILED);
        user.setSituacao(SituacaoUser.INATIVO);
        User userDesativado = iUser.save(user);
        return new ResponseEntity<User>(userDesativado, HttpStatus.OK);
    }

    public ResponseEntity<?> salvarUsuario(User userFront) {
        List<Role> role = userFront.getRoles();
        User user = iUser.findById(userFront.getId()).orElseThrow();

        if (role.isEmpty())
            role = Arrays.asList(iRole.findByName(Const.ROLE_ADMIN_ADMIN));

        user.setRoles(null);
        user = iUser.save(user);

        user.setRoles(role);
        user = iUser.save(user);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    /**
     * Função responsavel por salvar os novos usuários
     *
     * @param newUser
     * @return ResponseEntity<User>(userSaved, HttpStatus.OK);
     */
    public ResponseEntity<?> addNovoUsuario(User newUser) {
        String senhaProvisoria = this.geradorSenhaRandomica();

        User user = new User(newUser.getNomeCompleto(), newUser.getUser(), newUser.getEmail(),
                passwordEncoder.encode(senhaProvisoria), null, SituacaoUser.AGUARDANDO_ATIVACAO,
                new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));

        User userSaved = iUser.save(user);

        this.emailService.notificacaoNovoUsuario(userSaved, senhaProvisoria, urlTrocaSenha);

        return new ResponseEntity<User>(userSaved, HttpStatus.OK);
    }

    /**
     * Gera uma senha randomica para novos usuários
     *
     * @return String
     */
    private String geradorSenhaRandomica() {
        Integer numeroCaracteres = 20;

        StringBuilder AlphaNumericString = new StringBuilder()
                .append("ABCDEFGHIJKLMNOPQRSTUVWXYZ").append("0123456789").append("abcdefghijklmnopqrstuvxyz");

        StringBuilder sb = new StringBuilder(numeroCaracteres);

        for (int i = 0; i < numeroCaracteres; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.toString().charAt(index));
        }

        return sb.toString();
    }

    public ResponseEntity<?> changeUserPass(@NotNull TrocaSenhaUserDTO newUser) {
        try {
            User user = iUser.findById(newUser.getIdUser())
                    .filter(x -> x.getSituacao() == SituacaoUser.AGUARDANDO_ATIVACAO)
                    .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

            if (!user.getDateChangePassword().after(new Date())) {
                throw new RegraDeNegocioException("Data de prazo expirado para alterar senha.");
            }

            String newPassEncode = crypto.decryptText(newUser.getConfirmPass());
            if (!this.passwordEncoder.matches(newPassEncode, user.getPassword())) {
                throw new RegraDeNegocioException("A confirmação de senha não confere.");
            }

            user.setPassword(passwordEncoder.encode(crypto.decryptText(newUser.getNewPass())));
            user.setSituacao(SituacaoUser.ATIVO);
            user.setDateChangePassword(null);
            iUser.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RegraDeNegocioException(e.getMessage());
        }
    }

    public ResponseEntity<User> enviaEmailTrocaSenhaADM(Long idUser) {
        try {
            String senhaProvisoria = this.geradorSenhaRandomica();

            User user = iUser.findById(idUser).get();
            user.setSituacao(SituacaoUser.AGUARDANDO_ATIVACAO);
            user.setPassword(passwordEncoder.encode(senhaProvisoria));
            user.setDateChangePassword(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
            user = iUser.save(user);

            this.emailService.notificacaoNovoUsuario(user, senhaProvisoria, urlTrocaSenha);

            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraDeNegocioException("Ocorreu um erro ao enviar o e-mail para alteração de senha.");
        }
    }
}
