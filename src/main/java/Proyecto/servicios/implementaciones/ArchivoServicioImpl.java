package Proyecto.servicios.implementaciones;

import Proyecto.repositorios.CasoRepo;
import Proyecto.repositorios.CuentaRepo;
import Proyecto.servicios.interfaces.ArchivoServicio;
import Proyecto.servicios.interfaces.CuentaServicio;
import Proyecto.servicios.interfaces.EmailServicio;
import Proyecto.servicios.interfaces.FirebaseStorageService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ArchivoServicioImpl implements ArchivoServicio {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public ArchivoServicioImpl(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public String guardarArchivo(MultipartFile archivo) throws IOException {
        try {
            DBObject metadata = new BasicDBObject();
            metadata.put("contentType", archivo.getContentType());

            ObjectId id = gridFsTemplate.store(
                    archivo.getInputStream(),
                    archivo.getOriginalFilename(),
                    archivo.getContentType(),
                    metadata
            );
            return id.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }


}
