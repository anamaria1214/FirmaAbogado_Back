package Proyecto.repositorios;

import Proyecto.modelo.documentos.Caso;
import Proyecto.modelo.documentos.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepo extends MongoRepository<Comentario, String> {
}

