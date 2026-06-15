package com.spring.springbootapplication.mapper;

import com.spring.springbootapplication.domain.Category;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();
}
