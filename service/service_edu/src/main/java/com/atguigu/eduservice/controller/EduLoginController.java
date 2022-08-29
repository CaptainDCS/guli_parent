package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin //解决跨域问题
public class EduLoginController {
    //login
    @PostMapping("/login")
    public R login(){

        return R.ok().data("token","admin");
    }
    //info
    @GetMapping("/info")
    public R info(){

        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","D:\\Development\\IDEA\\BackgroundIMG\\4.jpg");
    }
}
