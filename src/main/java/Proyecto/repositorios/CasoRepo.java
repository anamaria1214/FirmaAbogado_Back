package Proyecto.repositorios;

import Proyecto.modelo.documentos.Caso;
import Proyecto.modelo.documentos.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasoRepo extends MongoRepository<Caso, String> {

    @Query("{idCliente:  ?0}")
    List<Caso> buscarCasosPorClientes(String idCliente);
}

