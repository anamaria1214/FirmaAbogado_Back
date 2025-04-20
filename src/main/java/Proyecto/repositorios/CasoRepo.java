package Proyecto.repositorios;

import Proyecto.modelo.documentos.Caso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasoRepo extends MongoRepository<Caso, String> {

    /**
     *
     * @param idCliente
     * @return
     */
    @Query("{idCliente:  ?0}")
    List<Caso> buscarCasosPorClientes(String idCliente);

    @Query("{ 'idAbogados': ?0 }")
    List<Caso> buscarCasosPorAbogado(String idAbogado);
}

