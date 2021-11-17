package personal.ch.trainbookingsystem.service;

import personal.ch.trainbookingsystem.entity.*;

import java.util.Date;
import java.util.List;

public interface AdminService {
    // 增加站点
    int addStation(String stationName);

    // 更新站点
    int updateStation(Long id, String stationName);

    // 查询站点
    List<Station> selectAllStation();

    // 查找单个站点
    Station selectSingleStation(Long stationId);

    // 删除单个站点
    int deleteSingleStation(Long stationId);

    // 查询全部用户
    List<Users> selectAllUsers();

    // 查询单个用户信息
    Users selectSingleUserInfo(Long userId);

    // 重置密码
    int resetPassword(String username);

    // 修改单个用户信息
    int updateSingleUserInfo(Long userId, String username, String name, String idNumber, String phone);

    // 查询单个用户信息,用户检查用户名是否相同
//    int selectSingleUsername(String username);

    //添加用户或管理员
    int addUser(String uname, String pwd, String names, String idNum, String telephone);

    // 为用户添加权限
    int addRoleUser(Long userId, Long roleId);

    // 通过用户名查询单个用户信息
    Users findByUsername(String username);

    // 删除用户信息
    public int deleteSingleUser(Long uid);

    // 查询全部路线图操作
    List<RoadMap> selectAllRoadMap();

    // 增加路线图
    int addRoadMap(String startStation, String endStation);

    // 通过id号查询单个路线图
    RoadMap findRoadMapByRoadMapId(Long roadMapId);

    // 更新路线图
    int updateRoadMap(Long roadMapId, String startStation, String endStation);

    // 删除路线图
    int deleteRoadMap(Long roadMapId);

    /**
     * 班次操作
     */
    // 查询全部的班次列表
    List<Train> selectAllTrain();

    // 查找单个班次
    Train findTrainById(Long id);

    // 通过火车编号查找单个班次
    Train findTrainByTrainNumber(String trainNumber);

    // 增加火车班次
    int addTrain(String trainNumber, String startTime, String endTime, Double price, Long ticketNumber, Long state, Long roadMapSelect);

    // 更新班次
    int updateTrain(Long trainId, String trainNumber, String startTime, String endTime, Double price,
                    Long ticketNumber, Long state, Long roadMapId);

    // 删除班次
    int deleteTrainById(Long id);

    /**
     * 订单操作
     */
    // 查询所有订单
    List<Order> findAllOrder();

    // 查询单个订单通过id查找
    Order findOrderById(Long orderId);

    // 更新订单信息
    int updateOrder(Long orderId, Long state);

    // 同意订单
    int agreeUpdateOrder(Long orderId);

    // 软删除订单
    int deleteOrderById(Long orderId);
}
