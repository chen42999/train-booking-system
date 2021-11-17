package personal.ch.trainbookingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Long ticketId;
    private Long ticketNumber;
    private Train train;
    private Users users;
    private Long state;
    private Long trainId;
    private Long userId;

}
