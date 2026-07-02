package com.spring.springbootapplication.mapper;

import com.spring.springbootapplication.domain.Category;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();
    Category findById(@Param("id") Long id);
}
