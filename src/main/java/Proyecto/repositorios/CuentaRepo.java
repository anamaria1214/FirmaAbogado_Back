package Proyecto.repositorios;

import Proyecto.modelo.documentos.Cuenta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepo extends MongoRepository<Cuenta, String> {

    @Query("{email:  ?0}")
    Optional<Cuenta> findByEmail(String email);
    @Query("{cedula:  ?0}")
    Optional<Cuenta> findByCedula(String cedula);


}

