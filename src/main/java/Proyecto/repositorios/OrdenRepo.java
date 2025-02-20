package Proyecto.repositorios;

import Proyecto.modelo.documentos.Comentario;
import Proyecto.modelo.documentos.Orden;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepo extends MongoRepository<Orden, String> {
}
