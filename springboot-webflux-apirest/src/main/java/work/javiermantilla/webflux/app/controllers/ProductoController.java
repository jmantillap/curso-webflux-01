package work.javiermantilla.webflux.app.controllers;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.webflux.app.models.documents.Producto;
import work.javiermantilla.webflux.app.models.services.ProductoService;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
	
	
	private final ProductoService service;
	
	@Value("${config.uploads.path}")
	private String path;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Producto>>> lista(){
		return Mono.just(
				ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.findAll())
				);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Producto>> ver(@PathVariable String id){
		return service.findById(id).map(p -> ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Producto> monoProducto){
		
		Map<String, Object> respuesta = new HashMap<>();		
		return monoProducto.flatMap(producto -> {
			if(producto.getCreateAt()==null) {
				producto.setCreateAt(new Date());
			}
			
			return service.save(producto).map(p-> {
				respuesta.put("producto", p);
				respuesta.put("mensaje", "Producto creado con éxito");
				respuesta.put("timestamp", new Date());
				return ResponseEntity
					.created(URI.create("/api/productos/".concat(p.getId())))
					.contentType(MediaType.APPLICATION_JSON)
					.body(respuesta);
				});
			
		}).onErrorResume(t -> {
			return Mono.just(t).cast(WebExchangeBindException.class)
					.flatMap(e -> Mono.just(e.getFieldErrors()))
					.flatMapMany(Flux::fromIterable)
					.map(fieldError -> "El campo "+fieldError.getField() + " " + fieldError.getDefaultMessage())
					.collectList()
					.flatMap(list -> {
						respuesta.put("errors", list);
						respuesta.put("timestamp", new Date());
						respuesta.put("status", HttpStatus.BAD_REQUEST.value());
						return Mono.just(ResponseEntity.badRequest().body(respuesta));
					});
							
		});
	}			
	

}
