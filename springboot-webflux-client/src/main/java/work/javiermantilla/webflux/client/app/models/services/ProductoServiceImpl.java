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

import org.springframework.core.io.buffer.DataBuffer;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
	

	//private final WebClient client;
	private final WebClient.Builder client;
	@Override
	public Flux<Producto> findAll() {
		return client.build().get().accept(MediaType.APPLICATION_JSON )
				.exchangeToFlux(response -> response.bodyToFlux(Producto.class))
				;
		
		
	}

	@Override
	public Mono<Producto> findById(String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return client.build().get().uri("/{id}", params)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Producto.class);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		
		return client.build().post()
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)				
				.bodyValue(producto)
				.retrieve()
				.bodyToMono(Producto.class);
	}

	@Override
	public Mono<Producto> update(Producto producto, String id) {
		
		return client.build().put()
				.uri("/{id}", Collections.singletonMap("id", id))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(producto)
				.retrieve()
				.bodyToMono(Producto.class);
	}

	@Override
	public Mono<Void> delete(String id) {
		return client.build().delete().uri("/{id}", Collections.singletonMap("id", id))
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
		
		return client.build().post()
				.uri("/upload/{id}", Collections.singletonMap("id", id))
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.bodyValue(partsBuilder.build())
				.retrieve()
				.bodyToMono(Producto.class);
	}

}
