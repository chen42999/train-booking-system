package personal.ch.trainbookingsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.ch.trainbookingsystem.entity.*;
import personal.ch.trainbookingsystem.repository.UserRepository;
import personal.ch.trainbookingsystem.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final int LIMIT = 10;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional // 开启事务支持
    public int saveUser(Users user) {
        // 为用户密码加密
        String encode = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encode);
        userRepository.saveUser(user);
        // 添加用户成功后，为用户添加权限
        return userRepository.saveUserRole(userRepository.findByUsername(user.getUsername()).getId());
    }

    @Override
    public int login(String username, String password) {
        return userRepository.login(username, password);
    }


    @Override
    public String findAuthorityByUsername(long userId) {
        return userRepository.findAuthorityByUsername(userId);
    }

    @Override
    public int saveUserRole(long userId) {
        return userRepository.saveUserRole(userId);
    }

    @Override
    public Users findUserInfoByUsername(String username) {
        return userRepository.findUserInfoByUsername(username);
    }

    @Override
    public int updateUserInfo(String username, String name, String idNumber, String phone) {
        return userRepository.updateUserInfo(username, name, idNumber, phone);
    }

    // 找回密码
    @Override
    public int retrievePasswordPassword(String username, String password) {
        String passwordEncode = new BCryptPasswordEncoder().encode(password);
        int flag = 0;
        int i = userRepository.retrievePasswordPassword(username, passwordEncode);
        if (i != 0){
            flag = 2;
        }
        return flag;
    }

    /**
     * 查询火车
     *
     * @param page  前台传来的原始分页
     * @param limit 总共显示10页
     * @return 返回List<Train>
     */
    @Override
    public List<Train> selectAllTrain(int page, int limit, String startTime, String endTime) {
        // 页数*显示数
        // 0---> 0 * 10 ---> limit 0, 10
        // 1---> 1 * 10 ---> limit 10,10
        page = page * limit;
        return userRepository.selectAllTrain(page, limit, startTime, endTime);
    }

    // 火车分页
    public int getPages() {
        int totalPages = userRepository.getPages();
        int page = 0;
        if (totalPages % LIMIT == 0) {
            page = totalPages / LIMIT;
        } else {
            page = totalPages / LIMIT + 1;
        }
        return page;
    }

    @Override
    public int getPages(String startTime) {
        int totalPages = userRepository.getPages(startTime);
        int page = 0;
        if (totalPages % LIMIT == 0) {
            page = totalPages / LIMIT;
        } else {
            page = totalPages / LIMIT + 1;
        }
        return page;
    }

    // 火车分页2
    @Override
    public int getTrainPages(int size) {
        int page = 0;
        if (size % LIMIT == 0) {
            page = size / LIMIT;
        } else {
            page = size / LIMIT + 1;
        }
        return page;
    }

    // 买票
    @Override
    @Transactional // 开启事务
    public int buyTicket(Long trainId) {
        // 0 => 失败，
        // 1 => 用户名已存在
        // 2 => 成功
        // 3 => 只能购买一张票
        // 4 => 火车票以售光
        int flag = 0;

        // 获取日期并进行转换为yyyy-MM-dd形式
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = simpleDateFormat.format(date);

        // 获取用户信息
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) principal;
        Users users = userRepository.findByUsername(user.getUsername());

        // if语句去掉保留下面语句，可解除购票限制
    //        int i = userRepository.buyTicket(trainId, users.getId(), formatDate);
    //        if (i != 0) {
    //            flag = 2;
    //        }

        // 加入购票限制
        if (users.getTicketLimit() < 1) {
            // 插入订单
            int i = userRepository.buyTicket(trainId, users.getId(), formatDate);
            if (i != 0) {
                flag = 2;
            }
        } else if (users.getTicketLimit() >= 1) {
            flag = 3;
        }

        return flag;
    }

    // 用户查询订单
    public List<Order> userFindOrderInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) principal;
        List<Order> orders = userRepository.userFindOrderInfo(user.getUsername());
        for (Order order : orders) {
            order.setTrain(userRepository.findOrderTrainByTrainId(order.getTrain().getId()));
        }
        return orders;
    }

    // 用户查询车票信息
    @Override
    public List<Ticket> findUserTicketInfo() {
        // 获取用户信息
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) principal;
        // 获得用户车票数据
        List<Ticket> userTicketInfo =
                userRepository.findUserTicketInfo(userRepository.findByUsername(user.getUsername()).getId());

        // 获得路线图数据
        // 将获得的路线图数据传给Ticket对象
        for (Ticket ticket : userTicketInfo) {
            Train orderTrainByTrainId = userRepository.findOrderTrainByTrainId(ticket.getTrain().getId());
            // 获得路线图与火车映射对象
            TrainRoadMap trainRoadMapById = userRepository.findTrainRoadMapById(orderTrainByTrainId.getId());
            // 获得路线图对象
            RoadMap roadMap = userRepository.findRoadMapById(trainRoadMapById.getRoadMapId());
            ticket.getTrain().setRoadMap(roadMap);

            // 已知bug
            // 当从数据库获取数据时，会将train表中的id也同时赋给users表中的id导致与ticket表中的数据不一致
            // 暂时解决方案之一，再次调用findByUsername()方法对ticket对象中的users.id重新赋值，使其恢复正常值
            // 该方法已测试，可行
            ticket.getUsers().setId(userRepository.findByUsername(user.getUsername()).getId());
        }
        return userTicketInfo;
    }

    // 用户退票操作
    @Transactional // 开启事务
    public int dishonourTrain(Long ticketId) {
        // 车票state状态值 // 0.软删除 // 1.已购买 // 2.已退票
        // flag 0.失败 2.成功操作
        int flag = 0;
        // 获得用户车票信息
        Ticket ticketByTicketId = userRepository.findTicketByTicketId(ticketId);
        // 获得火车信息
        Train orderTrainByTrainId = userRepository.findOrderTrainByTrainId(ticketByTicketId.getTrainId());
        // 退票使火车的余票+1
        // setTicketNumber/getTicketNumber是设置/获得火车余票方法
        orderTrainByTrainId.setTicketNumber(orderTrainByTrainId.getTicketNumber() + 1);
        int i = userRepository.dishonourTrain(ticketId, ticketByTicketId.getTrainId(), orderTrainByTrainId.getTicketNumber());
        if (i != 0) {
            flag = 2;
        }
        return flag;
    }

    // 用户改签，向用户展示初始地相同，并且日期在该日期及其以后都可以选择
    @Override
    public List<Train> findTrainByTrainIdList(Long trainId) {
        Train train = userRepository.findOrderTrainByTrainId(trainId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(train.getStartTime());
        return userRepository.findTrainByTrainIdList(train.getRoadMap().getStartStation(), format);
    }

    @Override
    public Ticket findTicketById(Long ticketId) {
        return userRepository.findTicketByTicketId(ticketId);
    }

    // 实现改签功能
    @Override
    @Transactional // 开启事务
    public int updateTrainTicketByTrainId(Ticket ticketId, Long sTicketId) {
        // 0--->失败
        // 2--->成功
        int flag = 0;
        // 实现改签
        int i = userRepository.updateTrainTicketByTrainId(ticketId.getTrainId(), sTicketId, ticketId.getTicketId());

        if (i != 0) {
            flag = 2;
        }
        return flag;
    }

    @Override
    public List<RoadMap> selectAllRoadMap() {
        return userRepository.selectAllRoadMap();
    }

    @Override
    public Train findRoadMapById(Long trainId) {
        return userRepository.findOrderTrainByTrainId(trainId);
    }
}
