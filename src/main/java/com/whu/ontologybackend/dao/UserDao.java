package com.whu.ontologybackend.dao;


import com.whu.ontologybackend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface UserDao {
    public List<User> selectAll();
}
