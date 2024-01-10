package com.whu.ontologybackend.controller;

import com.whu.ontologybackend.entity.User;
import com.whu.ontologybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/selectAll")
    public List<User> selectAll(){
        List<User> users = userService.selectAll();
        return users;
    }
}
