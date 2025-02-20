package Proyecto.repositorios;

import Proyecto.modelo.documentos.Abogado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbogadoRepo extends MongoRepository<Abogado, String> {
}

