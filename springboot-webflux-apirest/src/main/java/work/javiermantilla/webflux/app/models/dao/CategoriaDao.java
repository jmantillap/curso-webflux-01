package work.javiermantilla.webflux.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import work.javiermantilla.webflux.app.models.documents.Categoria;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String>{

}
