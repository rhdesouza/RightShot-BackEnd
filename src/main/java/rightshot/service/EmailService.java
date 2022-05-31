package rightshot.service;

import javax.mail.MessagingException;

import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;
import rightshot.entity.NF;
import rightshot.entity.PrecificacaoProduto;
import rightshot.entity.User;
import rightshot.entity.Venda;
import rightshot.repository.ClientesRepository;
import rightshot.repository.INFRepository;
import rightshot.repository.VendaRepository;
import rightshot.repository.UserRepository;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EmailService {

    @Autowired
    UserRepository iUser;

    @Autowired
    INFRepository iNf;

    @Autowired
    private InfoRSCService infoService;

    @Autowired
    private UtilService utilSerivce;

    @Autowired
    private ClientesRepository iCliente;

    @Autowired
    private VendaRepository vendaRepository;

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    @Async
    public boolean sendMail(User users) throws MessagingException {
        try {
            var user = iUser.findByUser("admin");

            Context context = new Context();
            context.setVariable("user", user);

            String process = templateEngine.process("welcome", context);
            javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setSubject("Welcome " + user.getNomeCompleto());
            helper.setText(process, true);
            helper.setTo(infoService.getEmailSocios());

            javaMailSender.send(mimeMessage);

            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    @Async
    public void enviaNotaFiscal(NF nota) {
        try {

            Context context = new Context();
            context.setVariable("nf", nota);

            String process = templateEngine.process("cadastroNF", context);
            javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setSubject("Right Shot Club - Nota Fiscal");
            helper.setText(process, true);
            helper.setTo(infoService.getEmailSocios());

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Async
    public void notificacaoGerarEstoque(NF nota) {
        try {

            Context context = new Context();
            context.setVariable("nf", nota);

            String process = templateEngine.process("notificacaoGerarEstoque", context);
            javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setSubject("Right Shot Club - Nota Fiscal");
            helper.setText(process, true);
            helper.setTo(infoService.getEmailSocios());

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Async
    public void enviaEmailPrecificacao(PrecificacaoProduto pp) {
        try {

            Context context = new Context();
            context.setVariable("pp", pp);
            context.setVariable("getValorMedioNF",
                    this.utilSerivce.formataValorMonetarioParaExibicao(pp.getValorMedioNF()));
            context.setVariable("getValorProdutoSugerido",
                    this.utilSerivce.formataValorMonetarioParaExibicao(pp.getValorProdutoSugerido()));
            context.setVariable("getValorProduto",
                    this.utilSerivce.formataValorMonetarioParaExibicao(pp.getValorProduto()));

            String process = templateEngine.process("precificacaoProduto", context);
            javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setSubject("Right Shot Club - Precificação de Produto");
            helper.setText(process, true);
            helper.setTo(infoService.getEmailSocios());

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Async
    public void enviaEmailVendaClienteAsync(Venda venda) {
        this.notificacaoVendaCliente(venda);
    }

    public Boolean notificacaoVendaCliente(Venda venda) {
        try {
            Context context = new Context();
            context.setVariable("venda", venda);

            String process = templateEngine.process("notificacaoVendaCliente", context);
            javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setSubject("Right Shot Club - Notificação de Compra");
            helper.setText(process, true);
            helper.setTo(venda.getCliente().getEmail());

            javaMailSender.send(mimeMessage);
            venda.setEmailEnviado(Boolean.TRUE);
            vendaRepository.save(venda);

            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.toString());
            venda.setEmailEnviado(Boolean.FALSE);
            vendaRepository.save(venda);
            return Boolean.FALSE;
        }
    }


    @Async
    public void notificacaoNovoUsuario(User user, String senhaProvisoria, String urlTrocaSenha) {
        try {
            Context context = new Context();
            context.setVariable("user", user);
            context.setVariable("passPrev", senhaProvisoria);
            context.setVariable("urlChangePass",
                    urlTrocaSenha.concat("?info=".concat(Base64.encode(user.toJson().getBytes(StandardCharsets.UTF_8)))));

            String process = templateEngine.process("novoUsuario", context);
            javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setSubject("Right Shot Club");
            helper.setText(process, true);
            helper.setTo(infoService.getEmailSocios());

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
