package rightShot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class RightShotApplication {

	public static void main(String[] args) {
		SpringApplication.run(RightShotApplication.class, args);
	}

	/**
	 * Configuração de Cors para funcionar em servers diferentes
	 * 
	 * @return
	 */
	@Bean
	public CorsFilter corsFilter() {
		/* config.addAllowedOrigin("http://localhost:8080"); */
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(false); //Configura se somente a url pode fazer as chamadas do back-end
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			log.info(
					"\r\n----------------------------------------------------------------------------------------------------------------\r\n"
							+ "██████╗ ██╗ ██████╗ ██╗  ██╗████████╗    ███████╗██╗  ██╗ ██████╗ ████████╗     ██████╗██╗     ██╗   ██╗██████╗ \r\n"
							+ "██╔══██╗██║██╔════╝ ██║  ██║╚══██╔══╝    ██╔════╝██║  ██║██╔═══██╗╚══██╔══╝    ██╔════╝██║     ██║   ██║██╔══██╗\r\n"
							+ "██████╔╝██║██║  ███╗███████║   ██║       ███████╗███████║██║   ██║   ██║       ██║     ██║     ██║   ██║██████╔╝\r\n"
							+ "██╔══██╗██║██║   ██║██╔══██║   ██║       ╚════██║██╔══██║██║   ██║   ██║       ██║     ██║     ██║   ██║██╔══██╗\r\n"
							+ "██║  ██║██║╚██████╔╝██║  ██║   ██║       ███████║██║  ██║╚██████╔╝   ██║       ╚██████╗███████╗╚██████╔╝██████╔╝\r\n"
							+ "╚═╝  ╚═╝╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝       ╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝        ╚═════╝╚══════╝ ╚═════╝ ╚═════╝\r\n"
							+ "----------------------------------------------------------------------------------------------------------------\r\n");
		};
	}

}
