package com.atguigu.eduservice.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 添加课程需要用到的表单字段
 */

@Data
public class CourseInfoVo {
    private String id;

    private String teacherId;

    private String subjectId;

    private String subjectParentId;

    private String title;

    private BigDecimal price;

    private Integer lessonNum;

    private String cover;

    private String description;
}
