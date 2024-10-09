package work.javiermantilla.webflux.app.models.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.webflux.app.models.documents.Categoria;
import work.javiermantilla.webflux.app.models.documents.Producto;

public interface ProductoService {

	public Flux<Producto> findAll();

	public Mono<Producto> findById(String id);

	public Mono<Producto> save(Producto producto);

	public Mono<Void> delete(Producto producto);

	public Flux<Producto> findAllConNombreUpperCase();

	public Flux<Producto> findAllConNombreUpperCaseRepeat();

	public Flux<Categoria> findAllCategoria();

	public Mono<Categoria> findCategoriaById(String id);

	public Mono<Categoria> saveCategoria(Categoria categoria);
	
	public Mono<Producto> findByNombre(String nombre);
	
	public Mono<Categoria> findCategoriaByNombre(String nombre);

}
