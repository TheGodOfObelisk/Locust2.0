package com.whu.ontologybackend.service;

import com.whu.ontologybackend.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<User> selectAll();
}
