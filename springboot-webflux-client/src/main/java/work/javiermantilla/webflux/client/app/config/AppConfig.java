package work.javiermantilla.webflux.client.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
	
	@Value("${config.base.endpoint}")
	private String url;
	
	@Bean
	@LoadBalanced
	WebClient.Builder registrarWebClient() {
		return WebClient.builder().baseUrl(url);
	}
	
//	@Bean	
//	WebClient registrarWebClient() {
//		return WebClient.create(url);
//	}
}
