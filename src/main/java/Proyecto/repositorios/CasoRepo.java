package Proyecto.repositorios;

import Proyecto.modelo.documentos.Caso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasoRepo extends MongoRepository<Caso, String> {
}

