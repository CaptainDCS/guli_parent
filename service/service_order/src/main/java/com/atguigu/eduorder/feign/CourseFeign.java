package com.atguigu.eduorder.feign;

import com.atguigu.commonutils.vo.CourseVo;
import com.atguigu.eduorder.feign.client.CourseClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-edu", fallback = CourseClient.class)
public interface CourseFeign {
    @GetMapping("/eduservice/courseFront/getCourseInfoById/{courseId}")
    public CourseVo getCourseInfoById(@PathVariable("courseId") String courseId);
}
