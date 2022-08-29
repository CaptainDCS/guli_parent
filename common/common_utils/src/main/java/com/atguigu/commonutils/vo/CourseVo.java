package com.atguigu.commonutils.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseVo {

    private String id;

    private String teacherName;

    private String subjectId;

    private String subjectParentId;

    private String title;

    private BigDecimal price;

    private Integer lessonNum;

    private String cover;

    private String description;
}
