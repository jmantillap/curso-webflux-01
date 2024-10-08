package work.javiermantilla.webflux.app;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import work.javiermantilla.webflux.app.models.documents.Categoria;
import work.javiermantilla.webflux.app.models.documents.Producto;
import work.javiermantilla.webflux.app.models.services.ProductoService;

@SpringBootApplication
@Log4j2
@RequiredArgsConstructor
public class SpringbootWebfluxApplication implements CommandLineRunner {

	private final ProductoService service;
	private final ReactiveMongoTemplate mongoTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebfluxApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		mongoTemplate.dropCollection("productos").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe();
		
		Categoria electronico = new Categoria("Electrónico");
		Categoria deporte = new Categoria("Deporte");
		Categoria computacion = new Categoria("Computación");
		Categoria muebles = new Categoria("Muebles");
		
		Flux.just(electronico, deporte, computacion, muebles)
		.flatMap(service::saveCategoria)
		.doOnNext(c ->{
			log.info("Categoria creada: " + c.getNombre() + ", Id: " + c.getId());
		}).thenMany(
				Flux.just(new Producto("TV Panasonic Pantalla LCD", 456.89, electronico),
						new Producto("Sony Camara HD Digital", 177.89, electronico),
						new Producto("Apple iPod", 46.89, electronico),
						new Producto("Sony Notebook", 846.89, computacion),
						new Producto("Hewlett Packard Multifuncional", 200.89, computacion),
						new Producto("Bianchi Bicicleta", 70.89, deporte),
						new Producto("HP Notebook Omen 17", 2500.89, computacion),
						new Producto("Mica Cómoda 5 Cajones", 150.89, muebles),
						new Producto("TV Sony Bravia OLED 4K Ultra HD", 2255.89, electronico)
						)
				.flatMap(producto -> {
					producto.setCreateAt(new Date());
					return service.save(producto);
					})
		)
		.subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));
		
	}

}
