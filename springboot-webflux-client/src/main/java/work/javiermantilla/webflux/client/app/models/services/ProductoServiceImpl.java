package work.javiermantilla.webflux.client.app.models.services;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.webflux.client.app.models.dto.Producto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;

import static org.springframework.http.MediaType.*;

// import static org.springframework.web.reactive.function.BodyInserters.*;



@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
	

	private final WebClient client;
	
	@Override
	public Flux<Producto> findAll() {
		
//		return client.get().accept(MediaType.APPLICATION_JSON )
//				.exchange()
//				.flatMapMany(response -> response.bodyToFlux(Producto.class));
		
		return client.get().accept(MediaType.APPLICATION_JSON )
				.exchangeToFlux(response -> response.bodyToFlux(Producto.class))
				;
		
		
	}

	@Override
	public Mono<Producto> findById(String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return client.get().uri("/{id}", params)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Producto.class);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		
		return client.post()
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				//.body(fromObject(producto))
				.bodyValue(producto)
				.retrieve()
				.bodyToMono(Producto.class);
	}

	@Override
	public Mono<Producto> update(Producto producto, String id) {
		
		return client.put()
				.uri("/{id}", Collections.singletonMap("id", id))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(producto)
				.retrieve()
				.bodyToMono(Producto.class);
	}

	@Override
	public Mono<Void> delete(String id) {
		return client.delete().uri("/{id}", Collections.singletonMap("id", id))
				.retrieve()
				.bodyToMono(Void.class);
	}

	@Override
	@SneakyThrows
	public Mono<Producto> upload(FilePart file, String id) {
		MultipartBodyBuilder partsBuilder = new MultipartBodyBuilder();
		partsBuilder.asyncPart("file",file.content() ,DataBuffer.class)
		.headers(h -> {
			h.setContentDispositionFormData("file", file.filename());
		});	
		
		return client.post()
				.uri("/upload/{id}", Collections.singletonMap("id", id))
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.bodyValue(partsBuilder.build())
				.retrieve()
				.bodyToMono(Producto.class);
	}

}
