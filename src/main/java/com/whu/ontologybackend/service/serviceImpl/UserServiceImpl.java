package com.whu.ontologybackend.service.serviceImpl;

import com.whu.ontologybackend.dao.UserDao;
import com.whu.ontologybackend.entity.User;
import com.whu.ontologybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> selectAll(){
        return userDao.selectAll();
    }
}
