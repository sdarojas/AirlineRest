package org.iesfm.airline;

import org.iesfm.airline.clients.FlightControllerClient;
import org.iesfm.airline.forms.flights.ShowFlightsForm;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setVisible(true);

        // Crea un retrofit que hace llamadas al localhost:8080
        // En ese puerto estára escuchando airline-rest
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Usando el retrofit anterior creamos un cliente para el controlador
        // de vuelos que está eschando en localhost:8080
        FlightControllerClient flightControllerClient =
                retrofit.create(FlightControllerClient.class);


        ShowFlightsForm form = new ShowFlightsForm(flightControllerClient);
        frame.setContentPane(form.mainPanel);
        frame.setTitle("Gestión de aerolinea");
        frame.pack();
        frame.repaint();
        frame.revalidate();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}