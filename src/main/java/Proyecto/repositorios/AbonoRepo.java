package Proyecto.repositorios;

import Proyecto.modelo.documentos.Abono;
import Proyecto.modelo.documentos.Caso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonoRepo extends MongoRepository<Abono, String> {
}
