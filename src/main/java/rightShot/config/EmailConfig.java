package rightShot.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class EmailConfig {
	
	@Bean
	public JavaMailSender getJavaMailSender(Environment env) {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    
	    mailSender.setHost(env.getProperty("api.mailSender.host"));
	    mailSender.setPort(Integer.parseInt(env.getProperty("api.mailSender.port")));
	    
	    mailSender.setUsername(env.getProperty("api.mailSender.userName"));
	    mailSender.setPassword(env.getProperty("api.mailSender.password"));
	    mailSender.setDefaultEncoding(env.getProperty("api.mailSender.encoding"));
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
}
