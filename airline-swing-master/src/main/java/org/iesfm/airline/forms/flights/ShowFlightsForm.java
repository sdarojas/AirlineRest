package org.iesfm.airline.forms.flights;

import org.iesfm.airline.clients.FlightControllerClient;
import org.iesfm.airline.clients.dto.FlightDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ShowFlightsForm {
    private FlightControllerClient flightControllerClient;
    public JPanel mainPanel;
    private FlightTableModel flightTableModel;
    private JTable flightsTable;
    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public ShowFlightsForm(FlightControllerClient flightControllerClient) {
        this.flightControllerClient = flightControllerClient;

        refreshFlights( );

        deleteButton.addActionListener(e -> {
            FlightDto flightDto = getSelectedFlightDto();

            if (flightDto == null) {
                JOptionPane.showMessageDialog(mainPanel, "No se puede eliminar, no hay vuelo seleccionado");
            } else {
                deleteFlight(flightDto);
            }
        });
        modifyButton.addActionListener(e -> {
            FlightDto flightDto = getSelectedFlightDto();
            if (flightDto == null) {
                JOptionPane.showMessageDialog(mainPanel, "No se puede modificar, no hay vuelo seleccionado");
            } else {
                showModifyDialog(flightDto);
            }
        });
        refreshButton.addActionListener(e -> {
            refreshFlights();
        });
        addButton.addActionListener(e -> {
            showAddDialog();
        });
    }

    private void showModifyDialog(FlightDto flightDto) {
        Window window = SwingUtilities.windowForComponent(mainPanel);
        JDialog showModifyDialog = new JDialog(window);
        ModifyFlightForm modifyFlightForm = new ModifyFlightForm(flightControllerClient, flightDto);
        showModifyDialog.setContentPane(modifyFlightForm.mainPanel);
        showModifyDialog.pack();
        showModifyDialog.setVisible(true);
        showModifyDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                refreshFlights();
            }
        });
    }
    private void showAddDialog() {
        Window window = SwingUtilities.windowForComponent(mainPanel);
        JDialog addDialog = new JDialog(window);
        AddFlightForm form = new AddFlightForm(flightControllerClient);
        addDialog.setContentPane(form.mainPanel);
        addDialog.pack();
        addDialog.setVisible(true);
        addDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                refreshFlights();
            }
        });
    }

    private void deleteFlight(FlightDto flightDto) {
        flightControllerClient
                .delete(flightDto.getId())
                .enqueue(
                        new Callback<>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.code() ==  200) {
                                    refreshFlights();
                                } else if(response.code() ==  404) {
                                    refreshFlights();
                                    JOptionPane.showMessageDialog(
                                            mainPanel,
                                            "No se ha podido eliminar el vuelo, no existe "
                                    );
                                } else {
                                    JOptionPane.showMessageDialog(
                                            mainPanel,
                                            "Ha habido un error al eliminar el vuelo: " + response.code()
                                    );
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable throwable) {
                                JOptionPane.showMessageDialog(
                                        mainPanel,
                                        "Ha habido un error al eliminar el vuelo: " + throwable.getMessage()
                                );
                            }
                        }
                );

    }

    private void refreshFlights() {
        flightControllerClient.list(
                null,
                null
        ).enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(
                            Call<List<FlightDto>> call,
                            Response<List<FlightDto>> response) {
                        if (response.code() == 200) {
                            // Esto se vuelve a ejecutar en el hilo de UI
                            List<FlightDto> flights = response.body();
                            flightTableModel = new FlightTableModel(flights);
                            flightsTable.setModel(flightTableModel);
                        } else {
                            JOptionPane.showMessageDialog(mainPanel, "No se han podido obtener los vuelos " + response.code());
                            refreshFlights();
                            flightsTable.repaint();
                            flightsTable.revalidate();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<FlightDto>> call,
                            Throwable throwable
                    ) {
                        JOptionPane.showMessageDialog(
                                mainPanel,
                                "Ha habido un errror al pedir los vuelos: " + throwable.getMessage()
                        );
                    }
                }
        );
    }

    private FlightDto getSelectedFlightDto() {
        if (flightsTable.getSelectedRow() == -1) {
            return null;
        } else {
            return flightTableModel
                    .getFlightAt(
                            flightsTable.getSelectedRow()
                    );
        }
    }
}
