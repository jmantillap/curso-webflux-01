package work.javiermantilla.reactor.app;


import java.time.Duration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.reactor.app.models.Comentario;
import work.javiermantilla.reactor.app.models.Usuario;
import work.javiermantilla.reactor.app.models.UsuarioComentario;

@SpringBootApplication
@Log4j2
public class SpringbootReactorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// this.ejemploIterable();
		//this.ejemploFlapMap();
		//this.ejemploToString();
		//ejemploCollectList();
		//ejemploUsuarioComentarioFlatMap();
		//ejemploUsuarioComentarioZipWithForma2();
		//ejemploZipWithRango();
		//ejemploInterval();
		//ejemploDelayElements();
		//ejemploIntervalInfinito();
		//ejemploIntervalDesdeCreate();
		ejemploManejarContrapresion();
	}
	
	@SneakyThrows
	public void ejemploManejarContrapresion() {
		
		Flux.range(1,10)
		.log()
		.subscribe(new Subscriber<Integer>() {			
			private Subscription s;
			private Integer limite = 5;
			private Integer consumido = 0;
			
			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				s.request(limite);				
			}

			@Override
			public void onNext(Integer t) {
				log.info(t.toString());
				consumido++;
				if(consumido.intValue() == limite.intValue()) {
					consumido = 0;
					s.request(limite);
				}
			}

			@Override
			public void onError(Throwable t) {}

			@Override
			public void onComplete() {}
			
		});		
		//i-> log.info(i.toString())
		
		Flux.range(1,10)
		.log()
		.limitRate(5)
		.subscribe();
		
	}
	
	@SneakyThrows
	public void ejemploIntervalDesdeCreate() {
		
		Flux.create(emiter->{
			Timer time= new Timer();
			time.schedule(new TimerTask() {
				private  Integer contador=0;
				@Override
				public void run() {
					emiter.next(++contador);
					if(contador==10) {
						time.cancel();
						emiter.complete();
					}
					if(contador==5) {
						time.cancel();
						emiter.error(new InterruptedException("Error, se ha detenido el flux en 5"));
					}
					
				}
			}, 1000, 1000);
		})
		//.doOnNext(next -> log.info(next.toString()))
		//.doOnComplete(()-> log.info("Hemos terminado"))
		.subscribe(next -> log.info(next.toString()),
					error-> log.error(error.getMessage()),
					()-> log.info("hemos terminado")
					)
		;
	}
	
	@SneakyThrows
	public void ejemploIntervalInfinito() {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux.interval(Duration.ofSeconds(1))
		.doOnTerminate(latch::countDown)
		.flatMap(i -> {
			if(i>=5) {
				return Flux.error(new InterruptedException("Solo hasta 5"));
			}else {
				return Flux.just(i);
			}
		})
		.map(i->"Hola" + i)
		.retry(2)
		.subscribe(s->log.info(s),e-> log.error(e.getMessage()));
		
		latch.await();
	}
	
	public void ejemploDelayElements() {
		Flux<Integer> rango = Flux.range(1,12)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i-> log.info(i.toString()))
				;		
		rango
		//.subscribe()
		.blockLast();// para visualizar
	}
	
	public void ejemploInterval() {
		Flux<Integer> rango = Flux.range(1,12);
		Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));
		
		rango.zipWith(retraso,(ra,re)-> ra )
		.doOnNext(i-> log.info(i.toString()))
		//.subscribe(i-> log.info(i.toString()))
		.blockLast();
		
		
	}
	
	public void ejemploZipWithRango() {
		Flux.just(1,2,3,4)
		.map(i->i*2)
		.zipWith(Flux.range(0, 4),(uno,dos)->String.format("Primer FLux: %d, Segundo flux: %d ",uno,dos))
		.subscribe(texto-> log.info(texto) )
		;
		
	}
	
	
	public void ejemploUsuarioComentarioZipWithForma2() {
		
		Mono<Usuario> usuarioMono = Mono.fromCallable(()->createUser());
		
		Mono<Comentario> comentarioMono= Mono.fromCallable(()->{
			Comentario comentario= new Comentario();
			comentario.addComentario("Comentario");
			comentario.addComentario("1 comentario");
			comentario.addComentario("2 comentario");
			return comentario;
		});
		
		Mono<UsuarioComentario> usuarioComentario= usuarioMono
				.zipWith(comentarioMono)
				.map(tuple->{
					Usuario u= tuple.getT1();
					Comentario c= tuple.getT2();
					return new UsuarioComentario(u, c);
				});		
		usuarioComentario.subscribe(uc-> log.info(uc.toString()));
		
	}	

	public void ejemploUsuarioComentarioZipWith() {
		
		Mono<Usuario> usuarioMono = Mono.fromCallable(()->createUser());
		
		Mono<Comentario> comentarioMono= Mono.fromCallable(()->{
			Comentario comentario= new Comentario();
			comentario.addComentario("Comentario");
			comentario.addComentario("1 comentario");
			comentario.addComentario("2 comentario");
			return comentario;
		});
		
		Mono<UsuarioComentario> usuarioComentario= usuarioMono
				.zipWith(comentarioMono,(usuario,comentarioUsuario) -> new UsuarioComentario(usuario, comentarioUsuario));		
		usuarioComentario.subscribe(uc-> log.info(uc.toString()));
		
	}	
	
	
	
	public void ejemploUsuarioComentarioFlatMap() {
		//Mono<Usuario> usuario = Mono.fromCallable(()->this.createUser());
		Mono<Usuario> usuarioMono = Mono.fromCallable(()->createUser());
		
		Mono<Comentario> comentarioMono= Mono.fromCallable(()->{
			Comentario comentario= new Comentario();
			comentario.addComentario("Comentario");
			comentario.addComentario("1 comentario");
			comentario.addComentario("2 comentario");
			return comentario;
		});
		
		usuarioMono.flatMap(u-> comentarioMono.map(c-> new UsuarioComentario(u,c)))
		.subscribe(uc-> log.info(uc.toString()));
		
	}	
	
	private Usuario createUser() {
		return new Usuario("ELiana","Bravo");
	}
	
	public void ejemploCollectList() throws Exception {

		List<Usuario> usuarioList = List
				.of( new Usuario("Andres", "guzman") , new Usuario("Pedro", "fulano"),
						new Usuario("Juan", "fulana"),new Usuario("Maria", "prueba"),
						new Usuario("Juan", "prueba") ,new Usuario("Bruce", "lee"),
						new Usuario("Bruce", "willis"));

		Flux.fromIterable(usuarioList)
		.collectList()
		.subscribe(lista-> {
			lista.forEach(i->{
				log.info(i.toString());
			});
		});
	}	
	
	public void ejemploToString() throws Exception {

		List<Usuario> usuarioList = List
				.of( new Usuario("Andres", "guzman") , new Usuario("Pedro", "fulano"),
						new Usuario("Juan", "fulana"),new Usuario("Maria", "prueba"),
						new Usuario("Juan", "prueba") ,new Usuario("Bruce", "lee"),
						new Usuario("Bruce", "willis"));



		Flux.fromIterable(usuarioList)
				.map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
				.flatMap(nombreApellido -> {
					if (nombreApellido.toLowerCase().contains("juan")) {
						return Mono.just(nombreApellido);
					} else {
						return Mono.empty();
					}
				})				
				.map(String::toLowerCase)
				.subscribe(nombreApellido -> log.info(nombreApellido));

	}

	public void ejemploFlapMap() throws Exception {

		List<String> usuarioList = List.of("Andres guzman", "Pedro fulano", "Juan fulana", "Maria prueba",
				"Juan prueba", "Bruce lee", "Bruce willis");

		
		Flux.fromIterable(usuarioList)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1]))
				.flatMap(usuario -> {
					if (usuario.getNombre().equalsIgnoreCase("juan")) {
						return Mono.just(usuario);
					} else {
						return Mono.empty();
					}
				})				
				.map(usuario -> new Usuario(usuario.getNombre().toLowerCase(), usuario.getApellido().toLowerCase()))
				.subscribe(usuario -> log.info(usuario.toString()));

	}

	public void ejemploIterable() throws Exception {

		Flux<Usuario> nombres = Flux.just("Andres", "Pedro", "Javier", "Maria", "Juan")
				.map(nombre -> new Usuario(nombre, null)).doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("nombre vacio");
					}
					System.out.println("Esto es: " + usuario.getNombre());
				}).map(usuario -> {
					return new Usuario(usuario.getNombre().toLowerCase(), null);
				});

		nombres.subscribe(usuario -> log.info(usuario.toString()), error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejeccion del observable");
					}
				});

		log.info("FILTER **********************************************");

		Flux<Usuario> usuarios = Flux
				.just("Andres guzman", "Pedro fulano", "Javier fulana", "Maria prueba", "Juan prueba", "Bruce lee",
						"Bruce willis")
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1]))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce")).doOnNext(usuario -> {
					if (usuario == null)
						throw new RuntimeException("nombre vacio");
					System.out.println("Esto es: " + usuario.getNombre());
				}).map(usuario -> new Usuario(usuario.getNombre().toLowerCase(), usuario.getApellido().toLowerCase()));

		usuarios.subscribe(usuario -> log.info(usuario.toString()), error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejeccion del observable");
					}
				});

		log.info("Inmutabilidad**************************************");

		Flux<String> nombreUsuarios = Flux.just("Andres guzman", "Pedro fulano", "Javier fulana", "Maria prueba",
				"Juan prueba", "Bruce lee", "Bruce willis");

		Flux<Usuario> usuarioFlux = nombreUsuarios
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce")).doOnNext(usuario -> {
					if (usuario == null)
						throw new RuntimeException("nombre vacio");
					System.out.println("Esto es: " + usuario.getNombre());
				}).map(usuario -> new Usuario(usuario.getNombre().toLowerCase(), usuario.getApellido().toLowerCase()));

		usuarioFlux.subscribe(usuario -> log.info(usuario.toString()), error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejeccion del observable");
					}
				});

		log.info("FLUX A PARTIR DE LIST**************************************");

		List<String> usuarioList = List.of("Andres guzman", "Pedro fulano", "Javier fulana", "Maria prueba",
				"Juan prueba", "Bruce lee", "Bruce willis");

		Flux<String> nombresFlux = Flux.fromIterable(usuarioList);
		nombresFlux.subscribe();
	}

}
