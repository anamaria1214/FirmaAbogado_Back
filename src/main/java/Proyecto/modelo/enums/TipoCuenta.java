package Proyecto.modelo.enums;

public enum TipoCuenta {

    ABOGADO(0),CLIENTE(1), ADMIN(2);

    private int id;
    private TipoCuenta(int id){
        this.id=id;
    }
}
