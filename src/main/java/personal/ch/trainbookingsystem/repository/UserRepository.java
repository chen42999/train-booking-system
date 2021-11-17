package personal.ch.trainbookingsystem.repository;

import org.apache.ibatis.annotations.Mapper;
import personal.ch.trainbookingsystem.entity.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserRepository {
    // 查询一个用户
//    @Select("select * from user where username = #{username}")
    Users findByUsername(String username);

    // 查询用户权限
    String findAuthorityByUsername(long userId);

    // 用户注册功能
    int saveUser(Users user);

    // 找回密码
    int retrievePasswordPassword(String username, String password);

    // 保存用户权限信息
    int saveUserRole(long userId);

    // 登录
    int login(String username, String password);

    // 查询用户个人信息
    Users findUserInfoByUsername(String username);

    // 更新用户个人信息
    int updateUserInfo(String username, String name, String idNumber, String phone);

    /**
     * 车次列表
     */
    // 查询全部的班次列表
    // 火车查询分页
    List<Train> selectAllTrain(int page, int limit, String startTime, String endTime);

    //火车分页总页数
    int getPages();

    // 火车分页总数
    int getPages(String startTime);

    /**
     * 用户操作
     */
    // 用户购买车票
    int buyTicket(Long trainId, Long userId, String createTime);

    // 用户查询订单信息
    List<Order> userFindOrderInfo(String username);

    // 获得路线
    RoadMap getRoadMap(Long roadMapId);

    // 查询全部路线图操作
    List<RoadMap> selectAllRoadMap();

    // 获得单个路线图
    RoadMap findRoadMapById(Long id);

    // 获得火车与路线图的映射
    public TrainRoadMap findTrainRoadMapById(Long trainId);

    // 通过Id获得火车与路线图关联的数据
    Train findOrderTrainByTrainId(Long trainId);

    // 通过用户id查看所有车票
    List<Ticket> findUserTicketInfo(Long userId);

    // 用户通过车票id查找
    Ticket findTicketByTicketId(Long ticketId);

    // 用户退票操作
    int dishonourTrain(Long ticketId, Long trainId, Long ticketsLeft);

    // 通过车票id获得车票（ticket）信息
    Ticket findTicketById(Long ticketId);

    // 用户改签，向用户展示初始地相同，并且日期在该日期及其以后都可以选择
    List<Train> findTrainByTrainIdList(String startStation, String startTime);

    /**
     * 实现用户改签功能
     *
     * @param pTrainId   火车ID
     * @param ticketId 车票ID
     * @param sTrainId 要修改的火车Id值
     * @return 返回int
     */
    int updateTrainTicketByTrainId(Long pTrainId, Long sTrainId, Long ticketId);

}
