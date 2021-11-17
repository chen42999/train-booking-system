package personal.ch.trainbookingsystem.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import personal.ch.trainbookingsystem.entity.Users;
import personal.ch.trainbookingsystem.repository.TestPage;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestSecurityController {

    @RequestMapping("/roleMethod")
    @ResponseBody
    @Secured({"ROLE_USER"})
//    @PreAuthorize("hasAnyAuthority('USER')")
    public String testRoleMethod(@RequestParam("stationName") String stationName) {
        return "hello roleMethod" + stationName;
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String testRoleAdmin(@RequestParam("stationName") String stationName, Model model) {
        return stationName;
    }


    @RequestMapping("/testJson")
    public String testJson() {
        return "Test";
    }

    @RequestMapping(value = "/json", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> jsonTest(@RequestBody Map<String, String> map) {
        System.out.println(map.get("name"));
        System.out.println(map.get("age"));
        System.out.println(map.containsKey("name"));
        return map;
    }

    @Autowired
    private TestPage testPage;

    @GetMapping("/getAllUser")
    public String getAllPerson(Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 5);
        List<Users> list = testPage.findAllUsers();
        PageInfo<Users> pageInfo = new PageInfo<Users>(list);
        model.addAttribute("pageInfo", pageInfo);
        return "Test";
    }


}
