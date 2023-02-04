package org.iesfm.airline.clients;

import org.iesfm.airline.clients.dto.FlightDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FlightControllerClient {

    @GET("/flights")
    Call<List<FlightDto>> list(
            @Query("origin") String  origin,
            @Query("destination") String  destination
    );
    @GET( "/flights/{flightId}")
    Call<FlightDto> getFlight(
            @Path("flightId") String flightId
    );

    @POST("/flights")
    Call<Void> add(
            @Body FlightDto flight
    );

    @DELETE("/flights/{flightId}")
    Call<Void> delete(@Path("flightId") String flightId);

    @PUT("/flights/{flightId}")
    Call<Void> update(
            @Path("flightId") String flightId,
            @Body FlightDto flightDto);
}
