package Proyecto.repositorios;

import Proyecto.modelo.documentos.Factura;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepo extends MongoRepository<Factura, String> {

    @Query("{idCaso:  ?0}")
    Optional<Factura> findByCaso(String idCaso);
}

