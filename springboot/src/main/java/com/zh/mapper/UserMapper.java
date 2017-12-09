package com.zh.mapper;

import com.zh.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@Mapper
public interface UserMapper {

    @Select("select * from t_user")
    List<User> findAll();

}
