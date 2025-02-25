package Proyecto.repositorios;

import Proyecto.modelo.documentos.Cliente;
import Proyecto.modelo.documentos.Cuenta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepo extends MongoRepository<Cliente, String> {

    @Query("{idCliente:  ?0}")
    Optional<Cliente> buscarCliente(String idCliente);
}

