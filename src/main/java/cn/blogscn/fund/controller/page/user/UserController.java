package cn.blogscn.fund.controller.page.user;

import cn.blogscn.fund.entity.user.User;
import cn.blogscn.fund.service.user.UserService;
import cn.blogscn.fund.util.JsonResult;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public JsonResult<Boolean> login(
            @RequestBody User user, HttpServletResponse resp) {
        if(user.getName().isEmpty() || user.getPassword().isEmpty()){
            return JsonResult.success(false);
        }
        User login = userService.login(user, resp);
        if(login == null){
            return JsonResult.success(false);
        }
        return JsonResult.success(true);
    }
}
