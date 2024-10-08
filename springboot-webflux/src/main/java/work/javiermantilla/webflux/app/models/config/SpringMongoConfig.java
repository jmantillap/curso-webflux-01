package work.javiermantilla.webflux.app.models.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import com.mongodb.reactivestreams.client.MongoClient;

@Configuration
public class SpringMongoConfig {

	@Value("${spring.data.mongodb.database}")
	private String dataBase; 	 
	//remove '_class’ field
	@Bean
	ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
		ReactiveMongoTemplate template = new ReactiveMongoTemplate(mongoClient, dataBase);
		MappingMongoConverter converter = (MappingMongoConverter) template.getConverter();
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		converter.afterPropertiesSet();
		return template;
	}
}