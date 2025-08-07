package org.examen.tecnico.lacomerexamentecnico.exception;

import java.io.Serial;

public class CopomexNoDisponibleException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public CopomexNoDisponibleException(String message) {
        super(message);
    }
}
