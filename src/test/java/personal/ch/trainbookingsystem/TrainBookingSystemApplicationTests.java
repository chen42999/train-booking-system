package personal.ch.trainbookingsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import personal.ch.trainbookingsystem.entity.Train;
import personal.ch.trainbookingsystem.entity.Users;
import personal.ch.trainbookingsystem.repository.AdminRepository;
import personal.ch.trainbookingsystem.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootTest
class TrainBookingSystemApplicationTests {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        List<Train> trains = adminRepository.selectAllTrain();
        for (int i = 0; i < trains.size(); i++) {
            System.out.println(trains.get(i).getRoadMap().getStartStation());
        }
    }

    @Test
    void test(){
        Train trainById = adminRepository.findTrainById(1L);
            System.out.println(trainById.getRoadMap().getStartStation());

    }

    @Test
    @Transactional  // 开启事务支持
    void test02(){
        Users users = new Users();
        users.setUsername("www");
        users.setPassword("123456");
        users.setIdNumber("50000");
        userRepository.saveUser(users);
        int z = 1 / 0;
        userRepository.saveUserRole(userRepository.findByUsername(users.getUsername()).getId());
    }

    @Test
    void test03(){
        List<Train> trains = userRepository.selectAllTrain(0, 10, "2021-10-24", "");
        int size = trains.size();
        System.out.println(size);
    }

    @Test
    void test04(){
//        List<Train> trainByTrainIdList = userRepository.findTrainByTrainIdList("北京", "2021-10-24");
//        for (Train trains :
//                trainByTrainIdList) {
//            System.out.println(trains);
//        }
//        Train orderTrainByTrainId = userRepository.findOrderTrainByTrainId(8L);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String format1 = format.format(orderTrainByTrainId.getStartTime());
//        List<Train> trainByTrainIdList = userRepository.findTrainByTrainIdList(orderTrainByTrainId.getRoadMap().getStartStation(), format1);
//        System.out.println(trainByTrainIdList + " size: " + trainByTrainIdList.size());

        System.out.println(adminRepository.updateOrder(3L, 7L, "", null));

    }

}
