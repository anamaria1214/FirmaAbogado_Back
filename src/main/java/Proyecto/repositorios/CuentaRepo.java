package Proyecto.repositorios;

import Proyecto.modelo.documentos.Cuenta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaRepo extends MongoRepository<Cuenta, String> {
}

