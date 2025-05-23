package Proyecto.servicios.interfaces;


import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ArchivoServicio {

    String guardarArchivo(MultipartFile archivo) throws IOException;


}
