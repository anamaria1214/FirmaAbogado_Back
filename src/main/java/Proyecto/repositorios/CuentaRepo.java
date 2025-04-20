package Proyecto.repositorios;

import Proyecto.modelo.documentos.Cuenta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepo extends MongoRepository<Cuenta, String> {

    /**
     * Busca una cuenta por su correo electrónico.
     *
     * @param email El correo electrónico asociado a la cuenta.
     * @return Un {@link Optional} que contiene la cuenta si se encuentra, o vacío si no existe.
     */
    @Query("{email:  ?0}")
    Optional<Cuenta> findByEmail(String email);

    /**
     * Busca una cuenta por su número de cédula.
     *
     * @param cedula La cédula (documento de identidad) asociada a la cuenta.
     * @return Un {@link Optional} que contiene la cuenta si se encuentra, o vacío si no existe.
     */
    @Query("{cedula:  ?0}")
    Optional<Cuenta> findByCedula(String cedula);


}

