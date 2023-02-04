package org.iesfm.airline.forms.flights;

import org.iesfm.airline.clients.dto.FlightDto;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FlightTableModel extends AbstractTableModel {


    private enum FlightTableColumns {
        // Aquí defino todas las columnas
        Id("Id"),
        Origin("Origen"),
        Destination("Destino"),
        Date("Fecha");

        // Estos son los campos de cada columna
        final String header;

        FlightTableColumns(String header) {
            this.header = header;
        }
    }

    private List<FlightDto> flights;

    public FlightTableModel(List<FlightDto> students) {
        super();
        this.flights = students;
    }

    @Override
    public int getRowCount() {
        return flights.size();
    }

    @Override
    public int getColumnCount() {
        return FlightTableColumns.values().length;
    }

    @Override
    public String getColumnName(int column) {
        return FlightTableColumns.values()[column].header;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FlightDto flights = this.flights.get(rowIndex);
        switch (FlightTableColumns.values()[columnIndex]) {
            case Id:
                return flights.getId();
            case Origin:
                return flights.getOrigin();
            case Destination:
                return flights.getDestination();
            case Date:
                return flights.getDate();
            default:
                throw new RuntimeException("No existe la columna " + columnIndex);
        }
    }

    public FlightDto getFlightAt(int row) {
        return flights.get(row);
    }
}