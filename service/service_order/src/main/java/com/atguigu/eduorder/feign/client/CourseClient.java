package com.atguigu.eduorder.feign.client;

import com.atguigu.commonutils.vo.CourseVo;
import com.atguigu.eduorder.feign.CourseFeign;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class CourseClient implements CourseFeign {
    @Override
    public CourseVo getCourseInfoById(String courseId) {
        throw new GuliException(20001, "系统被透了，无法获取到课程...");
    }
}
