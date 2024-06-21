package com.abimael.travelorder;

import java.time.temporal.ChronoUnit;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://localhost:8081/flight")
public interface FlightService {

    @GET
    @Path("findById")
    @Produces(MediaType.APPLICATION_JSON)
    Flight findById(@QueryParam("id") long id);

    @GET
    @Path("findByTravelOrderId")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 2)
    @Fallback(fallbackMethod = "fallback")
    @CircuitBreaker(
            requestVolumeThreshold = 4, // A cada 4 amostragens (requests) faça uma verificação
            failureRatio = 0.5, // qual a porcentagem de falha vou usar para assumir qeu existe um problema?
            delay = 5000,// Aguardar e depois voltar a deixar conectar (ligar o disjuntor)
            successThreshold = 2// quantas requisições com sucesso para eu assumir que o problema sumiu?
    )
    Flight findByTravelOrderId(@QueryParam("travelOrderId") long travelOrderId);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void newFlight(Flight flight);

    // O método de fallback tem que ter os mesmos parâmetros e tipo de retorno
    default Flight fallback(long travelOrderId) {
        Flight flight = new Flight();
        flight.setFromAirport("");
        flight.setToAirport("");
        flight.setTravelOrderId(travelOrderId);
        return flight;
    }
}
