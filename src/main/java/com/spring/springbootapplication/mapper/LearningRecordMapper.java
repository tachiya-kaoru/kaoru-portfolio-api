package com.spring.springbootapplication.mapper;

import com.spring.springbootapplication.domain.LearningRecord;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LearningRecordMapper {

    List<LearningRecord> findByUserIdAndYearAndMonth(
            @Param("userId") Long userId,
            @Param("learningYear") Integer learningYear,
            @Param("learningMonth") Integer learningMonth);

    int insert(LearningRecord learningRecord);

    int countByUserIdAndYearAndMonthAndItemName(
        @Param("userId") Long userId,
        @Param("learningYear") Integer learningYear,
        @Param("learningMonth") Integer learningMonth,
        @Param("itemName") String itemName);
}