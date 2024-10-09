package work.javiermantilla.webflux.app.models.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.webflux.app.models.dao.CategoriaDao;
import work.javiermantilla.webflux.app.models.dao.ProductoDao;
import work.javiermantilla.webflux.app.models.documents.Categoria;
import work.javiermantilla.webflux.app.models.documents.Producto;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductoServiceImpl implements ProductoService {

	private final ProductoDao dao;
	private final CategoriaDao categoriaDao;

	@Override
	public Flux<Producto> findAll() {
		return dao.findAll();
	}

	@Override
	public Mono<Producto> findById(String id) {
		return dao.findById(id);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		return dao.save(producto);
	}

	@Override
	public Mono<Void> delete(Producto producto) {
		return dao.delete(producto);
	}

	@Override
	public Flux<Producto> findAllConNombreUpperCase() {
		return dao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		});
	}

	@Override
	public Flux<Producto> findAllConNombreUpperCaseRepeat() {
		return findAllConNombreUpperCase().repeat(5000);
	}

	@Override
	public Flux<Categoria> findAllCategoria() {
		return categoriaDao.findAll();
	}

	@Override
	public Mono<Categoria> findCategoriaById(String id) {
		return categoriaDao.findById(id);
	}

	@Override
	public Mono<Categoria> saveCategoria(Categoria categoria) {
		return categoriaDao.save(categoria);
	}

	@Override
	public Mono<Producto> findByNombre(String nombre) {
		return dao.obtenerPorNombre(nombre);
	}

	@Override
	public Mono<Categoria> findCategoriaByNombre(String nombre) {
		return categoriaDao.findByNombre(nombre);
	}

}
