package com.zh.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/12/8.
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;



}
