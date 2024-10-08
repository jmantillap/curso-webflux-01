package work.javiermantilla.webflux.app.controllers;

//import java.time.Duration;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import reactor.core.publisher.Flux;
//import work.javiermantilla.webflux.app.models.dao.ProductoDao;
//import work.javiermantilla.webflux.app.models.documents.Producto;
//
//@Controller
//@RequiredArgsConstructor
//@Log4j2
//public class ProductoControllerV1 {
//	
//	private final ProductoDao dao;
//	
//	@GetMapping({"/listar", "/"})
//	public String listar(Model model) {
//		
//		Flux<Producto> productos = dao.findAll()
//				.map(producto -> {			
//					producto.setNombre(producto.getNombre().toUpperCase());
//					return producto;
//				});
//		
//		productos.subscribe(prod -> log.info(prod.getNombre()));
//		
//		model.addAttribute("productos", productos);
//		model.addAttribute("titulo", "Listado de productos");
//		return "listar";
//	}
//	
//	@GetMapping("/listar-datadriver")
//	public String listarDataDriver(Model model) {
//		
//		Flux<Producto> productos = dao.findAll().map(producto -> {
//			
//			producto.setNombre(producto.getNombre().toUpperCase());
//			return producto;
//		}).delayElements(Duration.ofSeconds(1));
//		
//		productos.subscribe(prod -> log.info(prod.getNombre()));
//		
//		model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 2));
//		model.addAttribute("titulo", "Listado de productos");
//		return "listar";
//	}
//	
//	
//	
//	@GetMapping("/listar-full")
//	public String listarFull(Model model) {
//		
//		Flux<Producto> productos = dao.findAll().map(producto -> {
//			
//			producto.setNombre(producto.getNombre().toUpperCase());
//			return producto;
//		}).repeat(5000);
//		
//		model.addAttribute("productos", productos);
//		model.addAttribute("titulo", "Listado de productos");
//		return "listar";
//	}
//	
//	@GetMapping("/listar-chunked")
//	public String listarChunked(Model model) {
//		
//		Flux<Producto> productos = dao.findAll().map(producto -> {
//			
//			producto.setNombre(producto.getNombre().toUpperCase());
//			return producto;
//		}).repeat(5000);
//		
//		model.addAttribute("productos", productos);
//		model.addAttribute("titulo", "Listado de productos");
//		return "listar-chunked";
//	}
//	
//	
//	
//}
