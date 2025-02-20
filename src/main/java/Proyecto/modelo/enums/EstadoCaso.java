package Proyecto.modelo.enums;

public enum EstadoCaso {

    ACTIVO(0),INACTIVO(1), TERMINADO(2);

    private int id;
    private EstadoCaso(int id){
        this.id=id;
    }
}
