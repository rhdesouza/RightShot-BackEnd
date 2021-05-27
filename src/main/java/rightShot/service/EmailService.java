package rightShot.service;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;
import rightShot.entity.NF;
import rightShot.entity.PrecificacaoProduto;
import rightShot.entity.User;
import rightShot.entity.Venda;
import rightShot.repository.ClientesRepository;
import rightShot.repository.INFRepository;
import rightShot.repository.VendaRepository;
import rightShot.repository.UserRepository;

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
	private VendaRepository iVenda;

	private final TemplateEngine templateEngine;
	private final JavaMailSender javaMailSender;

	public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
		this.templateEngine = templateEngine;
		this.javaMailSender = javaMailSender;
	}

	@Async
	public boolean sendMail(User user) throws MessagingException {
		try {
			user = iUser.findById(31L).get();

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
	public void enviaEmailVendaCliente(Long idVenda) {
		try {
			// Venda venda = iVenda.findByIdVendaCompleto(idVenda);
			Venda venda = iVenda.findById(idVenda).get();

			if (!venda.getCliente().getEmail().isEmpty()) {

				Context context = new Context();
				context.setVariable("venda", venda);
				context.setVariable("valorVenda",
						this.utilSerivce.formataValorMonetarioParaExibicao(venda.getValorTotalVenda()));
				context.setVariable("cpfCliente",
						this.utilSerivce.formataCpfCnpjParaExibicao(venda.getCliente().getCpf()));
				context.setVariable("dataHoraVenda",
						this.utilSerivce.formataDataHoraParaExibicao(venda.getDataHoraVenda()));

				String process = templateEngine.process("comunicacaoVenda", context);
				javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

				helper.setSubject("Right Shot Club - Comunicação de Compra (" + venda.getId() + ")");
				helper.setText(process, true);

				helper.setTo(venda.getCliente().getEmail());
				javaMailSender.send(mimeMessage);

				venda.setEmailEnviado(true);
				iVenda.save(venda);

			} else {
				venda.setEmailEnviado(false);
				iVenda.save(venda);
			}

		} catch (Exception e) {
			log.error(e.toString());
		}
	}
}
