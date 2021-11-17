package personal.ch.trainbookingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Train train;
    private Users user;
    private Date orderCreateTime;
    private Date orderOperateTime;
    private Long orderState;
    private Long orderAdminId;
    private String adminUsername;
    private Long trainId;
    private Long userId;
    private Long ticketId;

}
