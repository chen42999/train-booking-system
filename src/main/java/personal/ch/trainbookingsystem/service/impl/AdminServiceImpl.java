package personal.ch.trainbookingsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import personal.ch.trainbookingsystem.entity.*;
import personal.ch.trainbookingsystem.repository.AdminRepository;
import personal.ch.trainbookingsystem.service.AdminService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;


    @Override
    public int addStation(String stationName) {
        return adminRepository.addStation(stationName);
    }

    @Override
    public int updateStation(Long id, String stationName) {
        return adminRepository.updateStation(id, stationName);
    }

    @Override
    public List<Station> selectAllStation() {
        return adminRepository.selectAllStation();
    }

    @Override
    public Station selectSingleStation(Long stationId) {
        return adminRepository.selectSingleStation(stationId);
    }

    @Override
    public int deleteSingleStation(Long stationId) {
        return adminRepository.deleteSingleStation(stationId);
    }

    @Override
    public List<Users> selectAllUsers() {
        return adminRepository.selectAllUsers();
    }

    @Override
    public Users selectSingleUserInfo(Long userId) {
        return adminRepository.selectSingleUserInfo(userId);
    }

    @Override
    public int resetPassword(String username) {
        // 0 => 失败，
        // 1 => 用户名已存在
        // 2 => 成功
        int flag = 0;
        int i = adminRepository.resetPassword(username);
        if (i != 0){
            flag = 2;
        }
        return flag;
    }

    @Override
    public int updateSingleUserInfo(Long id, String username, String name, String idNumber, String phone) {
        return adminRepository.updateSingleUserInfo(id, username, name, idNumber, phone);
    }

//    @Override
//    public int selectSingleUsername(String username) {
//        return adminRepository.selectSingleUsername(username);
//    }

    @Override
    public int addUser(String uname, String pwd, String names, String idNum, String telephone) {
        // 对用户密码加密
        String pwdEncode = new BCryptPasswordEncoder().encode(pwd);
        return adminRepository.addUser(uname, pwdEncode, names, idNum, telephone);
    }

    @Override
    public int addRoleUser(Long userId, Long roleId) {
        return adminRepository.addRoleUser(userId, roleId);
    }

    @Override
    public Users findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    // 删除用户和权限
    // 在mybatis里同时执行两条删除语句
    @Override
    public int deleteSingleUser(Long uid) {
        return adminRepository.deleteSingleUser(uid);
    }

    // 查询全部的路线图
    @Override
    public List<RoadMap> selectAllRoadMap() {
        return adminRepository.selectAllRoadMap();
    }

    // 增加单个路线图
    @Override
    public int addRoadMap(String startStation, String endStation) {
        // 0 => 失败，
        // 1 => 用户名已存在
        // 2 => 成功
        int flag = 0;
        RoadMap roadMapByStationNames = adminRepository.findRoadMapByStationNames(startStation, endStation);
        if (roadMapByStationNames == null) {
            int i = adminRepository.addRoadMap(startStation, endStation);
            if (i != 0) {
                flag = 2; // 成功
            }
        } else {
            flag = 1; // 存在
        }

        return flag;
    }

    @Override
    public RoadMap findRoadMapByRoadMapId(Long roadMapId) {
        return adminRepository.findRoadMapByRoadMapId(roadMapId);
    }

    // 更新路线图
    @Override
    public int updateRoadMap(Long roadMapId, String startStation, String endStation) {
        // 0 => 失败，
        // 1 => 用户名已存在
        // 2 => 成功
        int flag = 0;
        RoadMap roadMapByStationNames = adminRepository.findRoadMapByStationNames(startStation, endStation);
        if (roadMapByStationNames == null) {
            int i = adminRepository.updateRoadMap(roadMapId, startStation, endStation);
            if (i != 0) {
                flag = 2;
            }
        } else {
            flag = 1;
        }
        return flag;
    }

    // 删除路线图
    @Override
    public int deleteRoadMap(Long roadMapId) {
        // 0 => 失败，
        // 1 => 用户名已存在
        // 2 => 成功
        int flag = 0;
        int i = adminRepository.deleteRoadMap(roadMapId);
        if (i != 0) {
            flag = 2;
        }
        return flag;
    }

    @Override
    public List<Train> selectAllTrain() {
        return adminRepository.selectAllTrain();
    }

    @Override
    public Train findTrainById(Long id) {
        Train trainById = adminRepository.findTrainById(id);
        trainById.getRoadMap().setStationEquals(trainById.getRoadMap().toString());
        int i = 0;
        return trainById;
    }

    /**
     * 通过火车编号查找
     *
     * @param trainNumber 火车编号
     * @return Train
     */
    @Override
    public Train findTrainByTrainNumber(String trainNumber) {
        return adminRepository.findTrainByTrainNumber(trainNumber);
    }

    /**
     * 增加火车班次
     *
     * @param trainNumber   火车编号
     * @param startTime     发车时间
     * @param endTime       到达时间
     * @param price         车票价格
     * @param ticketNumber  车票剩余塑料
     * @param state         班次状态
     * @param roadMapSelect roadMapId
     * @return int 返回int
     */
    @Override
    public int addTrain(String trainNumber, String startTime, String endTime, Double price, Long ticketNumber, Long state, Long roadMapSelect) {
        // 0 => 失败，
        // 1 => 用户名已存在
        // 2 => 成功
        int flag = 0;
        Train trainByTrainNumber = adminRepository.findTrainByTrainNumber(trainNumber);
        if (trainByTrainNumber == null) {
            int i = adminRepository.addTrain(trainNumber, startTime, endTime, price, ticketNumber, state);
            if (i != 0) {
                Train train = adminRepository.findTrainTableByTrainNumber(trainNumber);
                int j = adminRepository.addTrainRoadMap(train.getId(), roadMapSelect);
                if (j != 0) {
                    flag = 2;
                }
            }
        } else {
            flag = 1;
        }
        return flag;
    }

    // 更新班次
    @Override
    public int updateTrain(Long trainId, String trainNumber, String startTime, String endTime, Double price,
                           Long ticketNumber, Long state, Long roadMapId) {
        int flag = 0;
        int i = adminRepository.updateTrain(trainId, trainNumber, startTime, endTime, price, ticketNumber, state, roadMapId);
        if (i != 0) {
            flag = 2;
        }
        return flag;
    }

    @Override
    public int deleteTrainById(Long id) {
        int flag = 0;
        int i = adminRepository.deleteTrainById(id);
        if (i != 0) {
            flag = 2;
        }
        return flag;
    }

    // 管理员查询所有订单
    @Override
    public List<Order> findAllOrder() {
        List<Order> allOrder = adminRepository.findAllOrder();

        for (Order order : allOrder) {
            // 通过id查询火车与路线图的映射，并对Order对象中的Train对象赋值
            order.setTrain(adminRepository.findTrainById(order.getTrainId()));
            // 拿到用户或管理员信息，并为User对象赋值
            order.setUser(adminRepository.selectSingleUserInfo(order.getUserId()));
        }
        return allOrder;
    }

    @Override
    public Order findOrderById(Long orderId) {
        Order orderById = adminRepository.findOrderById(orderId);
        // 通过id查询火车与路线图的映射，并对Order对象中的Train对象赋值
        orderById.setTrain(adminRepository.findTrainById(orderById.getTrainId()));
        // 拿到用户或管理员信息，并为User对象赋值
        orderById.setUser(adminRepository.selectSingleUserInfo(orderById.getUserId()));
        return orderById;
    }

    // 更新订单信息
    public int updateOrder(Long orderId, Long state){
        int flag = 0;
        // 获得当前管理员信息
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) principal;
        Users username = adminRepository.findByUsername(user.getUsername());
        // 获得修改时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        int i = adminRepository.updateOrder(orderId, username.getId(), format, state);
        if (i != 0){
            flag = 2;;
        }
        return flag;
    }

    // 同意订单
    @Override
    public int agreeUpdateOrder(Long orderId){
        int flag = 0;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) principal;
        Users byUsername = adminRepository.findByUsername(user.getUsername());

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);

        int i = adminRepository.agreeUpdateOrder(orderId, byUsername.getId(), format);
        if (i != 0){
            flag = 2;
        }
        return flag;
    }

    // 软删除订单
    @Override
    public int deleteOrderById(Long orderId) {
        int flag = 0;
        int i = adminRepository.deleteOrderById(orderId);
        if (i != 0){
            flag = 2;
        }
        return flag;
    }
}
