package work.javiermantilla.webflux.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.webflux.app.models.dao.ProductoDao;
import work.javiermantilla.webflux.app.models.documents.Producto;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Log4j2
public class ProductoRestController {

	private final ProductoDao dao;

	@GetMapping
	public Flux<Producto> index(){

        Flux<Producto> productos = dao.findAll()
        		.map(producto -> {
        			producto.setNombre(producto.getNombre().toUpperCase());
        			return producto;
        			})
        		.doOnNext(prod -> log.info(prod.getNombre()));
        
        return productos;
	}

	@GetMapping("/{id}")
	public Mono<Producto> show(@PathVariable String id){
		
		/* Mono<Producto> producto = dao.findById(id); */		
		Flux<Producto> productos = dao.findAll();
		
		Mono<Producto> producto = productos
				.filter(p -> p.getId().equals(id))
				.next()
				.doOnNext(prod -> log.info(prod.getNombre()));
				
		return producto;
	}
	
}
