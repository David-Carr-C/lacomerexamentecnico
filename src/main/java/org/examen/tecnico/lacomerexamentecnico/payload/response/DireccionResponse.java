package org.examen.tecnico.lacomerexamentecnico.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionResponse{
    String calle;
    String numeroExterior;
    String numeroInterior;
    String codigoPostal;
    String colonia;
    String municipio;
    String estado;
    String pais;
    LocalDateTime fechaCreacion;
    LocalDateTime fechaActualizacion;
}