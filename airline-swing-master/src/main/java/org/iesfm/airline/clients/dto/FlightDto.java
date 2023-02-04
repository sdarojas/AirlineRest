package org.iesfm.airline.clients.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private String id;
    private String origin;
    private String destination;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;

}
