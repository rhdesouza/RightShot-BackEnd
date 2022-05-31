package rightshot.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import rightshot.entity.SituacaoUser;
import rightshot.entity.User;
import rightshot.repository.UserRepository;
import rightshot.service.EmailService;
import rightshot.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository iUser;

    @MockBean
    private EmailService emailService;

    @BeforeEach
    void preparaMock() {
        Mockito.when(iUser.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        //Mockito.when(emailService.notificacaoNovoUsuario(any(User.class), any(String.class), any(String.class))).thenReturn(null);
    }

    @Test
    void salvarNovoUsuario_quandoChamado() {
        User user = new User();
        user.setUser("test");
        user.setEmail("teste@test.com");
        user.setNomeCompleto("test");

        User userSaved = (User) userService.addNovoUsuario(user).getBody();

        Assertions.assertEquals(userSaved.getSituacao(), SituacaoUser.AGUARDANDO_ATIVACAO);
        Assertions.assertNotEquals(user.getPassword(), userSaved.getPassword());
    }


}
