package personal.ch.trainbookingsystem.service;

import personal.ch.trainbookingsystem.entity.*;

import java.util.List;

public interface UserService {
    // 查询一个用户
//    @Select("select * from user where username = #{username}")
    Users findByUsername(String username);

    // 用户注册功能
    int saveUser(Users user);

    // 用户登录功能
    int login(String username, String password);

    // 获取用户权限信息
    String findAuthorityByUsername(long userId);

    // 保存用户权限信息
    int saveUserRole(long userId);

    // 火车分页
    int getPages();

    // 火车分页总数
    int getPages(String startTime);

    // 火车分页2
    public int getTrainPages(int size);

    /**
     * 用户行为操作
     */
    // 查询用户个人信息
    Users findUserInfoByUsername(String username);

    // 更新用户个人信息
    int updateUserInfo(String username, String name, String idNumber, String phone);

    // 找回密码
    int retrievePasswordPassword(String username, String password);

    // 查询全部的班次列表
    List<Train> selectAllTrain(int page, int limit, String startTime, String endTime);

    // 买票
    int buyTicket(Long trainId);

    // 用户查询订单
    public List<Order> userFindOrderInfo();

    // 用户查看车票
    List<Ticket> findUserTicketInfo();

    // 用户退票操作
    int dishonourTrain(Long ticketId);

    // 查询全部路线图操作
    List<RoadMap> selectAllRoadMap();

    /**
     * 通过火车ID获得火车信息
     *
     * @param trainId 火车ID
     * @return Train对象
     */
    Train findRoadMapById(Long trainId);

    // 用户改签，向用户展示初始地相同，并且日期在该日期及其以后都可以选择
    List<Train> findTrainByTrainIdList(Long trainId);

    // 通过车票id获得车票（ticket）信息
    Ticket findTicketById(Long ticketId);

    /**
     * 实现用户改签
     * @param ticketId 车票ID
     * @return 返回int
     */
    int updateTrainTicketByTrainId(Ticket ticketId, Long pTicketId);

}
