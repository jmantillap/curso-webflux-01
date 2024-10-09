package work.javiermantilla.webflux.app.models.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;
import work.javiermantilla.webflux.app.models.documents.Producto;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

	public Mono<Producto> findByNombre(String nombre);

	@Query("{ 'nombre': ?0 }")
	public Mono<Producto> obtenerPorNombre(String nombre);
}
