package org.examen.tecnico.lacomerexamentecnico.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {
    Long id;
    String nombre;
    String apellidoPaterno;
    String apellidoMaterno;
    String correo;
    DireccionResponse direccion;
    LocalDateTime fechaCreacion;
    LocalDateTime fechaActualizacion;
}
