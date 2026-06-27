package com.spring.springbootapplication.mapper;

import com.spring.springbootapplication.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    long countByEmail(@Param("email") String email);

    int insert(User user);

    User findByEmail(@Param("email") String email);
	
	void update(User user);
}