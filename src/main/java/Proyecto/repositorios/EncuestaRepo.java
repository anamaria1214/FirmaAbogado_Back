package Proyecto.repositorios;

import Proyecto.modelo.documentos.Encuesta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncuestaRepo extends MongoRepository<Encuesta, String> {
}
