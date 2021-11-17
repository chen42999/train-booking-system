package personal.ch.trainbookingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {

    private Long id;
    private String trainNumber;
    private Date startTime;
    private Date endTime;
    private Double price;
    private Long ticketNumber;
    private Long state;

    private RoadMap roadMap;
    private Long roadMapId;

}
