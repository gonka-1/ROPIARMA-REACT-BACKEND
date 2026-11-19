package backend.ropiarma.models.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
}
