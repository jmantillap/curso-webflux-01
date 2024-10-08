package work.javiermantilla.reactor.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioComentario {
	private Usuario usuario;
	private Comentario comentario;
}
