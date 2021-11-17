package personal.ch.trainbookingsystem.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import personal.ch.trainbookingsystem.entity.*;
import personal.ch.trainbookingsystem.service.AdminService;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Secured({"ROLE_ADMIN"})
    @RequestMapping({"/", "/index", "/adminIndex"})
    public String adminIndex() {
        return "admin/index";
    }

    /**
     * 站点操作
     */

    // 跳转到站点
    @RequestMapping("/toStation")
    public String toStation() {
        return "admin/station";
    }

    // 添加站点
    @PostMapping("/station")
    public String addStation(@Param("stationName") String stationName, Model model) {
        int i = adminService.addStation(stationName);
        System.out.println("addStation: " + stationName);
        return "admin/station";
    }

    // 更新站点
    @RequestMapping("/updateStation")
    @ResponseBody
    public boolean updateStation(@Param("stationId") Long stationId, @Param("stationName") String stationName) {
        boolean flag = false;
        int i = adminService.updateStation(stationId, stationName);
        if (i != 0) {
            flag = true;
        }
        System.out.println("updateStation: " + i);
        return flag;
    }

    // 查询全部站点
    @RequestMapping("/selectAllStation")
    public String selectAllStation(Model model, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, 6);
        List<Station> stations = adminService.selectAllStation();
        PageInfo<Station> pageInfo = new PageInfo<>(stations);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/selectStation";
    }

    // 查询当个站点
    @RequestMapping(value = "/selectSingleStation", method = RequestMethod.POST)
    public String selectSingleStation(@Param("stationId") Long stationId, Model model) {
        Station station = adminService.selectSingleStation(stationId);
        model.addAttribute("singleStation", station);
        return "admin/selectStation";
    }

    /**
     * 查询单个站点信息
     * 往模态框传值而调用的方法
     * @param stationId
     * @return Station
     */
    @GetMapping(value = "/selectSingleStation")
    @ResponseBody
    public Station returnSingleStation(@RequestParam("stationId") Long stationId) {
        return adminService.selectSingleStation(stationId);
    }

    // 删除站点信息
    @RequestMapping("/deleteSingleStation")
    @ResponseBody
    public boolean deleteSingleStation(@RequestParam("stationId") String stationIdStr) {
        Long stationId = Long.valueOf(stationIdStr);
        boolean flag = false;
        int i = adminService.deleteSingleStation(stationId);
        if (i != 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 用户操作
     * @return
     */

    // 跳转到用户管理
    // 查询全部用户信息
    @RequestMapping("/toUser")
    public String toUserManagement(Model model, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, 6);
        List<Users> users = adminService.selectAllUsers();
        PageInfo<Users> pageInfo = new PageInfo<>(users);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/users";
    }

    // 实现添加用户信息
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public int addUser(@RequestParam("uname") String uname,
                       @RequestParam("pwd") String pwd, @RequestParam("names") String names,
                       @RequestParam("idNum") String idNum, @RequestParam("telephone") String telephone,
                       @RequestParam("userRoleId") Long userRoleId) {
        // 0 => 失败，
        // 1 => 用户名已存在
        // 2 => 成功
        int flag = 0;
        // 检查用户名是否相同
        // 用户名不相同就为null
        // 用户名相同就不为null
        Users users = adminService.findByUsername(uname);
        if (users == null) {
            // 添加用户操作
            int i1 = adminService.addUser(uname, pwd, names, idNum, telephone);
            // 判断是否添加成功
            if (i1 != 0) {
                // 通过用户名查找新添加用户的信息
                Users username = adminService.findByUsername(uname);
                // 为新添加的用户赋予权限
                adminService.addRoleUser(username.getId(), userRoleId);
                flag = 2;
            }
        } else {
            flag = 1;
        }
        return flag;
    }

    // 查询单个用户信息
    @RequestMapping(value = "/selectSingleUserInfo", method = RequestMethod.POST)
    public String selectSingleUserInfo(@RequestParam("userId") Long userId, Model model) {
        Users user = adminService.selectSingleUserInfo(userId);
        model.addAttribute("user", user);
        return "admin/users";
    }

    /**
     * 查询单个用户信息
     * 往模态框里传值
     *
     * @param id 用户的Id
     * @return Users
     */
    @GetMapping("/returnSingleUserInfo")
    @ResponseBody
    public Object selectSingleUserInfo(@RequestParam("userId") Long id) {
        return adminService.selectSingleUserInfo(id);
    }

    // 更新当个用户信息
    @RequestMapping("/updateSingleUser")
    @ResponseBody
    public boolean updateSingleUser(@Param("userId") Long userId, @Param("username") String username,
                                    @Param("name") String name,
                                    @Param("idNumber") String idNumber, @Param("phone") String phone) {
        boolean flag = false;
        int i = adminService.updateSingleUserInfo(userId, username, name, idNumber, phone);
        System.out.println(i);
        if (i != 0) {
            flag = true;
        }
        return flag;
    }

    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    @ResponseBody
    public int resetPassword(@RequestParam("username") String username){
        return adminService.resetPassword(username);
    }

    // 删除用户信息
    @RequestMapping("/deleteSingleUser")
    @ResponseBody
    public int deleteSingleUser(@RequestParam("uid") Long uid) {

        // 0 => 失败，
        // 1 => 用户名已存在
        // 2 => 成功
        int flag = 0;
        int i = adminService.deleteSingleUser(uid);
        if (i != 0) {
            flag = 2;
        }

        return flag;
    }

//    // 查询全部用户信息
//    @RequestMapping("/selectAllUsers")
//    public String selectAllUsers(Model model){
//        List<Users> users = adminService.selectAllUsers();
//        model.addAttribute("userList", users);
//        return "admin/users";
//    }

    /**
     * 路线图操作
     */

    // 跳转到路线图
    @RequestMapping("/toRoadMap")
    public String toRoadMap(Model model, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, 6 );
        List<RoadMap> roadMaps = adminService.selectAllRoadMap();
        List<Station> stations = adminService.selectAllStation();
        PageInfo<RoadMap> pageInfo = new PageInfo<>(roadMaps);
        model.addAttribute("addStationList", stations);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/roadMap";
    }

    // 实现添加路线图
    @RequestMapping(value = "/addRoadMap", method = RequestMethod.POST)
    @ResponseBody
    public int addRoadMap(@RequestParam("startStation") String startStation,
                          @RequestParam("endStation") String endStation) {
        return adminService.addRoadMap(startStation, endStation);
    }

    // 用于模态框传值
    @RequestMapping("/findRoadMap")
    @ResponseBody
    public RoadMap findRoadMapByRoadMapId(@RequestParam("roadMapId") Long roadMapId) {
        return adminService.findRoadMapByRoadMapId(roadMapId);
    }

    // 更新路线图
    @RequestMapping(value = "/updateRoadMap", method = RequestMethod.PUT)
    @ResponseBody
    public int updateRoadMap(@RequestParam("roadId") Long roadMapId,
                             @RequestParam("startStation") String startStation,
                             @RequestParam("endStation") String endStation) {
        return adminService.updateRoadMap(roadMapId, startStation, endStation);
    }

    // 删除路线图
    @RequestMapping("/deleteRoadMap")
    @ResponseBody
    public int deleteRoadMap(@RequestParam("roadMapId") Long roadMapId) {
        return adminService.deleteRoadMap(roadMapId);
    }

    /**
     * 车次操作
     */
    // 查询全部车次
    @RequestMapping("/toTrain")
    public String toTrain(Model model, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, 6 );
        List<Train> trains = adminService.selectAllTrain();
        List<RoadMap> roadMaps = adminService.selectAllRoadMap();
        PageInfo<Train> pageInfo = new PageInfo<>(trains);
        model.addAttribute("roadMapList", roadMaps);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/train";
    }

    // 车次，向模态框传值
    @RequestMapping("/findTrainById")
    @ResponseBody
    public Train findTrainById(@RequestParam("trainId") Long id) {
        return adminService.findTrainById(id);
    }

    /**
     * 增加班次
     *
     * @param trainNumber
     * @param startTime
     * @param endTime
     * @param price
     * @param ticketNumber
     * @param state
     * @param roadMapSelect
     * @return
     */
    @RequestMapping(value = "/addTrain", method = RequestMethod.POST)
    @ResponseBody
    public int addTrain(@RequestParam("trainNumber") String trainNumber, @RequestParam("startTime") String startTime,
                        @RequestParam("endTime") String endTime, @RequestParam("trainPrice") Double price,
                        @RequestParam("balanceNumber") Long ticketNumber,
                        @RequestParam("trainState") Long state, @RequestParam("roadMapSelect") Long roadMapSelect) {
        return adminService.addTrain(trainNumber, startTime, endTime, price, ticketNumber, state, roadMapSelect);
    }

    @RequestMapping(value = "/updateTrain", method = RequestMethod.PUT)
    @ResponseBody
    public int updateTrain(@RequestParam("trainId") Long trainId, @RequestParam("trainNumber") String trainNumber, @RequestParam("startTime") String startTime,
                           @RequestParam("endTime") String endTime, @RequestParam("price") Double price,
                           @RequestParam("ticketNumber") Long ticketNumber,
                           @RequestParam("state") Long state, @RequestParam("roadMapSelectId") Long roadMapId) {
        return adminService.updateTrain(trainId, trainNumber, startTime, endTime, price, ticketNumber, state, roadMapId);
    }

    @RequestMapping("/deleteTrainId")
    @ResponseBody
    public int deleteTrainById(@RequestParam("trainId") Long id) {
        return adminService.deleteTrainById(id);
    }

    /**
     * 订单操作
     */
    @RequestMapping("/toOrder")
    public String toOrder(Model model) {
        List<RoadMap> roadMaps = adminService.selectAllRoadMap();
        List<Order> allOrder = adminService.findAllOrder();
        for (Order order : allOrder) {
            if (order.getOrderAdminId() == 0){
                order.setAdminUsername("空");
            } else {
                order.setAdminUsername(adminService.selectSingleUserInfo(order.getOrderAdminId()).getUsername());
            }
        }
        model.addAttribute("roadMapList", roadMaps);
        model.addAttribute("orderList", allOrder);
        return "admin/order";
    }

    // 订单，往模态框传值
    @RequestMapping(value = "/findOrderById")
    @ResponseBody
    public Order findSingleOrderById(@RequestParam(value = "orderId") Long id) {
//        Long orderId = orderMap.get("oid");
        Order order = adminService.findOrderById(id);
        Long orderAdminId = order.getOrderAdminId();
        if (order.getOrderAdminId() == 0){
            order.setAdminUsername("空");
        } else {
            order.setAdminUsername(adminService.selectSingleUserInfo(orderAdminId).getUsername());
        }

        return order;
    }

    // 更新订单信息
    @RequestMapping(value = "/updateOrder")
    @ResponseBody
    public int updateOrder(@RequestParam("oid") Long orderId,
                           @RequestParam(value = "roadMapSelectId", required = false) Long rodaMapId,
                           @RequestParam(value = "startTime", required = false) String startTime,
                           @RequestParam(value = "endTime", required = true) String endTime,
                           @RequestParam(value = "username", required = false) String username,
                           @RequestParam(value = "ordercreatetime", required = false) String createTime,
                           @RequestParam(value = "adminusername", required = false) String adminUsername,
                           @RequestParam(value = "orderupdatetime", required = false) String updateTime,
                           @RequestParam("state") Long state) {

        return adminService.updateOrder(orderId, state);
    }

    // 同意订单
    @RequestMapping(value = "/agreeOrder", method = RequestMethod.GET)
    @ResponseBody
    public int agreementOrder(@RequestParam("oid") Long orderId) {
        return adminService.agreeUpdateOrder(orderId);
    }

    // 软删除订单
    @RequestMapping("/deleteOrder")
    @ResponseBody
    public int deleteOrderById(@RequestParam("oid") Long orderId){
        return adminService.deleteOrderById(orderId);
    }
}