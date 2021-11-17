package personal.ch.trainbookingsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
//@MapperScan("personal.ch.trainbookingsystem.repository")
@MapperScan("personal.ch.trainbookingsystem.repository")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class TrainBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainBookingSystemApplication.class, args);
    }

}
