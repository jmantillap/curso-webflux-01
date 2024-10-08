package work.javiermantilla.reactor.app.models;

import java.util.ArrayList;
import java.util.List;


import lombok.Data;

@Data
public class Comentario {
	List<String> comentarios = new ArrayList<>();
	
	public void addComentario(String comentario) {
		comentarios.add(comentario);
	}
	
}
