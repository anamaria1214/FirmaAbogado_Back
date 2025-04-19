package Proyecto.servicios.implementaciones;

import Proyecto.servicios.interfaces.FirebaseStorageService;

import com.google.cloud.firestore.Blob;
import com.google.firebase.cloud.StorageClient;
import io.opencensus.metrics.export.Distribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {


    @Override
    public String subirArchivo(MultipartFile multipartFile) throws IOException {
        /*

        Bucket bucket = StorageClient.getInstance().bucket();


        String fileName = String.format( "%s-%s", UUID.randomUUID().toString(), multipartFile.getOriginalFilename() );


        Blob blob = bucket.create( fileName, multipartFile.getInputStream(), multipartFile.getContentType() );


        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                blob.getName()
        );*/
        return null;
    }


}