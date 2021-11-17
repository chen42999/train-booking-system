package personal.ch.trainbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import personal.ch.trainbookingsystem.entity.*;
import personal.ch.trainbookingsystem.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    private final int LIMIT = 10;
    private String startTime;
    private Ticket ticketById;

    @Autowired
    private UserService userService;

    // 首页显示车站
    @RequestMapping({"/", "/index"})
    public String index(Model model, HttpServletRequest request) {
        int page = 0;
        String pageStr = "";
        if (request.getParameter("page") != null) {
            if (!request.getParameter("page").equals(""))
                pageStr = request.getParameter("page");
            page = Integer.parseInt(pageStr);
        }

        // 当用户点击全部查询时,清空时间
        if (request.getParameter("selectAll") != null) {
            startTime = "";
        }

        // 当用户选中要查看火车的时间和选中的起始站和终点站时
//        String startTime = null;
        String endTime = "";

        if (request.getParameter("startTime") != null && !request.getParameter("startTime").equals("")) {
            startTime = request.getParameter("startTime");
        }

        List<Train> trains = userService.selectAllTrain(page, LIMIT, startTime, endTime);
        // 得到分页总数
        int pages = userService.getPages(startTime);
        model.addAttribute("pages", pages);
        model.addAttribute("trainList", trains);
        return "index";
    }

    // 跳转到用户查看个人信息
    @RequestMapping("/toUserInfo")
    public String toUserInfo(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) principal;
        Users userInfoByUsername = userService.findUserInfoByUsername(user.getUsername());
        model.addAttribute("userInfo", userInfoByUsername);
        return "userInfo";
    }

    // 当用户要改签时，向模态框传值，获得当前用户已知的火车相关信息
    @RequestMapping("/getTrainRoadMap")
    @ResponseBody
    public Train getRoadMap(@RequestParam("ticketId") Long id) {
        ticketById = userService.findTicketById(id);
        return userService.findRoadMapById(ticketById.getTrainId());
    }

    // 更新用户信息
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public String updateUserInfo(@RequestParam("username") String username, @RequestParam("name") String name,
                                 @RequestParam("idNumber") String idNumber,
                                 @RequestParam("phone") String phone) {

        userService.updateUserInfo(username, name, idNumber, phone);
        return "index";
    }

    // 用户买票
    @RequestMapping("/buyTicket")
    @ResponseBody
    public int buyTicket(@RequestParam("trainId") Long trainId) {
        return userService.buyTicket(trainId);
    }

    // 用户查询订单信息
    @RequestMapping("/toMyTicket")
    public String toMyTicket() {
        return "myTicket";
    }

    // 得到分页
    @RequestMapping("/getPages")
    @ResponseBody
    public int getPages() {
        return userService.getPages();
    }

    // 用户查询订单
    @RequestMapping("/userFindOrderInfo")
    public String userFindOrderInfo(Model model) {
        List<Order> orders = userService.userFindOrderInfo();
        model.addAttribute("orderList", orders);
        return "myOrder";
    }

    // 用户查询车票信息
    @RequestMapping("/findUserTicketInfo")
    public String findUserTicketInfo(Model model) {
        List<Ticket> userTicketInfo = userService.findUserTicketInfo();
        List<RoadMap> roadMaps = userService.selectAllRoadMap();
        model.addAttribute("roadMapList", roadMaps);
        model.addAttribute("ticketList", userTicketInfo);
        return "myTicket";
    }

    // 用户退票操作
    @RequestMapping("/dishonourTrain")
    @ResponseBody
    public int dishonourTrain(@RequestParam("ticketId") Long ticketId) {
        return userService.dishonourTrain(ticketId);
    }

    // 跳转到改签页面
    // 用户改签，向用户展示初始地相同，并且日期在该日期及其以后都可以选择
    @RequestMapping(value = "/toTicketChang", method = RequestMethod.POST)
    public String toTicketChang(@RequestParam("trainId") Long trainId, Model model, HttpServletRequest request) {
        int page = 0;
        String pageStr = "";
        if (request.getParameter("page") != null) {
            if (!request.getParameter("page").equals(""))
                pageStr = request.getParameter("page");
            page = Integer.parseInt(pageStr);
        }

        // 当用户点击全部查询时,清空时间
        if (request.getParameter("selectAll") != null) {
            startTime = "";
        }

        // 当用户选中要查看火车的时间时
//        String startTime = null;
//        if (request.getParameter("startTime") != null && !request.getParameter("startTime").equals("")) {
//            startTime = request.getParameter("startTime");
//        }

        List<Train> trainByTrainIdList = userService.findTrainByTrainIdList(trainId);
        Date startTime = trainByTrainIdList.get(0).getStartTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(startTime);
        // 得到分页总数
        int pages = userService.getPages(String.valueOf(format));

        model.addAttribute("pages", pages);
        model.addAttribute("trainList", trainByTrainIdList);
        return "ticketChang";
    }

    /**
     * 实现用户改签
     * @param sTrainId 前台传来要修改的火车ID
     * @return 返回int
     */
    @RequestMapping(value = "/updateTrainTicket", method = RequestMethod.POST)
    @ResponseBody
    int updateTrainTicketByTrainId(@RequestParam("trainId") Long sTrainId){
        System.out.println(ticketById);
        return userService.updateTrainTicketByTrainId(ticketById, sTrainId);
    }

    // 跳转到登录页面
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    // 实现登录功能,由security实现
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    // 跳转到注册页面
    @RequestMapping("/toRegister")
    public String toRegister() {
        return "register";
    }

    // 实现注册功能
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("username") String username, @RequestParam("password") String password,
                           @RequestParam("name") String name, @RequestParam("idNumber") String idNumber,
                           @RequestParam("phone") String phone) {
        Users user = new Users(username, password, name, idNumber, phone);
        int i = userService.saveUser(user);
        System.out.println(i + ": " + user.getName() + user.getPassword());
        return "index";
    }

    // 跳转到找回密码
    @RequestMapping("/toRetrievePwd")
    public String toRetrievePassword(){
        return "retrievePwd";
    }

    // 跳转到找回密码
    @RequestMapping(value = "/retrievePwd", method = RequestMethod.POST)
    public String retrievePassword(@RequestParam("username") String username,
                                   @RequestParam("password") String password){
        userService.retrievePasswordPassword(username, password);
        return "retrievePwd";
    }


    // 管理员首页
    @RequestMapping("/adminIndex")
    public String adminIndex() {
        return "admin/index";
    }


    // 测试热部署，可删
    @RequestMapping("/testDev")
    @ResponseBody
    public String testDev() {
        return "Test DevTools 1111";
    }

//    // 测试用例，要删
//    @RequestMapping("/hello")
//    public String hello(){
//        return "/hello/hello";
//    }

}
