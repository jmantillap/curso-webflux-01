package work.javiermantilla.webflux.app.models.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
	
	@Id
	@NotEmpty
	private String id;
	
	private String nombre;
	
	public Categoria(String nombre) {
		this.nombre = nombre;
	}

}
	
