package com.jGod.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jGod.reggie.common.R;
import com.jGod.reggie.entity.User;
import com.jGod.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpServletRequest request){
        String phone = map.get("phone").toString();
        if(phone==null) return R.error("手机号不能为空");
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getPhone,phone);
        User user = userService.getOne(lambdaQueryWrapper);
        if(user==null){
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        request.getSession().setAttribute("user",user.getId());
        return R.success(user);
    }
}
