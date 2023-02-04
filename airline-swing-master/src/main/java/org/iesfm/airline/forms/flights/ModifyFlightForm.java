package org.iesfm.airline.forms.flights;

import org.iesfm.airline.clients.FlightControllerClient;
import org.iesfm.airline.clients.dto.FlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ModifyFlightForm {
    private final static Logger log = LoggerFactory.getLogger(ModifyFlightForm.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private FlightControllerClient flightControllerClient;
    private FlightDto flightDto;
    public JPanel mainPanel;
    private JTextField flightIdTextField;
    private JTextField originTextField;
    private JTextField destinationTextField;
    private JTextField dateTextField;
    private JButton acceptButton;
    private JButton cancelButton;

    public ModifyFlightForm(FlightControllerClient flightControllerClient, FlightDto flightDto) {
        this.flightControllerClient = flightControllerClient;
        this.flightDto = flightDto;

        flightIdTextField.setText(flightDto.getId());
        originTextField.setText(flightDto.getOrigin());
        destinationTextField.setText(flightDto.getDestination());
        dateTextField.setText(sdf.format(flightDto.getDate()));

        cancelButton.addActionListener(e -> {
            dispose();
        });
        acceptButton.addActionListener(e -> {
            updateFlight();
        });
    }

    private void dispose() {
        Window window = SwingUtilities.windowForComponent(mainPanel);
        window.dispose();
    }

    private void updateFlight() {
        try {
            FlightDto modifiedFlightDto = new FlightDto(
                    flightIdTextField.getText(),
                    originTextField.getText(),
                    destinationTextField.getText(),
                    sdf.parse(dateTextField.getText())
            );

            flightControllerClient
                    .update(flightDto.getId(), modifiedFlightDto)
                    .enqueue(
                            new Callback<>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() == 200) {
                                        dispose();
                                    } else if (response.code() == 404) {
                                        JOptionPane
                                                .showMessageDialog(mainPanel,
                                                        "No se ha podido modificar el vuelo, no existe"
                                                );
                                        dispose();
                                    } else {
                                        log.error("No se ha podido modificar el vuelo " + response);
                                        JOptionPane.showMessageDialog(mainPanel,
                                                "No se ha modificado. Error " + response.code()
                                        );
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable throwable) {
                                    log.error("No se ha podido modificar el vuelo", throwable);
                                    JOptionPane
                                            .showMessageDialog(mainPanel,
                                                    "No se ha podido modificar el vuelo. Error " + throwable.getMessage()
                                            );

                                }
                            }
                    );

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(mainPanel, "Fecha con formato incorrecto, debe seguir el formato yyyy/MM/dd");
        }
    }
}
