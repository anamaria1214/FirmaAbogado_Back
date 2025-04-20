package Proyecto.repositorios;

import Proyecto.modelo.documentos.Caso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasoRepo extends MongoRepository<Caso, String> {

    /**
     * Busca todos los casos asociados a un cliente específico mediante su ID.
     *
     * @param idCliente El identificador único del cliente.
     * @return Lista de objetos {@link Caso} que pertenecen al cliente con el ID proporcionado.
     */
    @Query("{idCliente:  ?0}")
    List<Caso> buscarCasosPorClientes(String idCliente);

    /**
     * Busca todos los casos en los que un abogado específico está asignado.
     *
     * @param idAbogado El identificador único del abogado.
     * @return Lista de objetos {@link Caso} en los que el abogado con el ID proporcionado está asignado.
     */
    @Query("{ 'idAbogados': ?0 }")
    List<Caso> buscarCasosPorAbogado(String idAbogado);
}

