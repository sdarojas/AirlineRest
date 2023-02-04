package org.iesfm.airline.forms.flights;

import org.iesfm.airline.clients.FlightControllerClient;
import org.iesfm.airline.clients.dto.FlightDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFlightForm {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public JPanel mainPanel;
    private JTextField idTf;
    private JButton addButton;
    private JButton cancelButton;
    private JTextField originTf;
    private JTextField destinyTf;
    private JTextField dateTf;
    private FlightControllerClient flightControllerClient;

    public AddFlightForm(FlightControllerClient flightControllerClient) {
        this.flightControllerClient = flightControllerClient;


        addButton.addActionListener(e -> {


            try {
                String id = idTf.getText();
                String origin = originTf.getText();
                String destiny = destinyTf.getText();
                Date date = sdf.parse(dateTf.getText());
                FlightDto flightDto = new FlightDto(id, origin, destiny, date);

                flightControllerClient.add(flightDto).enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {
                                    dispose();
                                }
                                else if (response.code() == 409) {
                                    JOptionPane.showMessageDialog(mainPanel,
                                            "No se ha podido añadir el vuelo " + response.code());

                                }else {
                                    JOptionPane.showMessageDialog(mainPanel,
                                            "No se ha podido añadir el vuelo,Error " + response.code());

                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable throwable) {
                                JOptionPane
                                        .showMessageDialog(mainPanel,
                                                "No se ha podido modificar el vuelo. Error " + throwable.getMessage()
                                        );
                            }
                        }
                );
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Fecha con formato dd/MM/yyyy");
            }


        });
    }

    private void dispose() {
        Window window = SwingUtilities.windowForComponent(mainPanel);
        window.dispose();

    }
}
