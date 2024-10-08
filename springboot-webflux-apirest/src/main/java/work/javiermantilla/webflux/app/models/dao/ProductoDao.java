package work.javiermantilla.webflux.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import work.javiermantilla.webflux.app.models.documents.Producto;



public interface ProductoDao extends ReactiveMongoRepository<Producto, String>{

}
