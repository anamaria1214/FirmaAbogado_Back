package Proyecto.dtos;

public record CambiarPasswordDTO(String codigoVerificacion, String email, String passwordNueva, String repetirContraseña) {
}