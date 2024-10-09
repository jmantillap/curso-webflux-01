package work.javiermantilla.webflux.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;
import work.javiermantilla.webflux.app.models.documents.Categoria;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String>{
	public Mono<Categoria> findByNombre(String nombre);
}
