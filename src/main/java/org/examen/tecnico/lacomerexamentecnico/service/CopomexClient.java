package org.examen.tecnico.lacomerexamentecnico.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.examen.tecnico.lacomerexamentecnico.exception.ColoniaNoEncontradaException;
import org.examen.tecnico.lacomerexamentecnico.exception.CopomexNoDisponibleException;
import org.examen.tecnico.lacomerexamentecnico.payload.response.DireccionCopomexResponse;
import org.examen.tecnico.lacomerexamentecnico.payload.response.CopomexResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CopomexClient {
    private final WebClient webClient;
    private final String token;

    public CopomexClient(WebClient.Builder builder,
                         @Value("${copomex.base-url}") String baseUrl,
                         @Value("${copomex.token}") String token) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.token = token;
    }

    /**
     * Completa la información de una dirección a partir de CP + colonia + calle + número.
     * @throws ColoniaNoEncontradaException si la colonia no pertenece al CP
     */
    @CircuitBreaker(name = "copomex", fallbackMethod = "completarDireccionFallback")
    public Mono<DireccionCopomexResponse> completarDireccion(String codigoPostal,
                                                             String coloniaUsuario,
                                                             String calle,
                                                             String numeroExterior,
                                                             String numeroInterior) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query/info_cp/{cp}")
                        .queryParam("type", "simplified") // 1 solo resultado agrupado
                        .queryParam("token", token)
                        .build(codigoPostal))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Error al consultar Copomex: " + clientResponse.statusCode())))
                .bodyToMono(CopomexResponse.class)
                .flatMap(root -> {                       // ahora sí, dentro del objeto raíz
                    if (root.isError()) {
                        return Mono.error(new RuntimeException(
                                "COPOMEX error: " + root.getMensajeError()));
                    }

                    CopomexResponse.CopomexData data = root.getResponse();
                    boolean ok = data.getColonias().stream()
                            .anyMatch(c -> c.equalsIgnoreCase(coloniaUsuario));

                    if (!ok) {
                        return Mono.error(new ColoniaNoEncontradaException("La colonia '" + coloniaUsuario
                                + "' no pertenece al CP " + codigoPostal));
                    }

                    return Mono.just(DireccionCopomexResponse.builder()
                            .codigoPostal(data.getCp())
                            .colonia(coloniaUsuario)
                            .municipio(data.getMunicipio())
                            .estado(data.getEstado())
                            .ciudad(data.getCiudad())
                            .calle(calle)
                            .numeroExterior(numeroExterior)
                            .numeroInterior(numeroInterior)
                            .build());

                });
    }

    private Mono<DireccionCopomexResponse> completarDireccionFallback(
            String codigoPostal,
            String coloniaUsuario,
            String calle,
            String numeroExterior,
            String numeroInterior,
            Throwable ex) {

        return Mono.error(new CopomexNoDisponibleException(
                "Servicio de direcciones temporalmente no disponible. Intenta más tarde."));
    }
}
