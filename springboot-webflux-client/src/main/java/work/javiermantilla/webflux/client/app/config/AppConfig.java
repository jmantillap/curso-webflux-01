package work.javiermantilla.webflux.client.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
	
	@Value("${config.base.endpoint}")
	private String url;
	
	@Bean
	WebClient registrarWebClient() {
		return WebClient.create(url);
	}
}
