package org.examen.tecnico.lacomerexamentecnico.exception;

import java.io.Serial;

public class NoResponseCopomexException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public NoResponseCopomexException(String message) {
        super(message);
    }
}
