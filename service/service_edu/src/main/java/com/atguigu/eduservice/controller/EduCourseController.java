package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author captainM
 * @since 2022-08-17
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
@Transactional
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @PostMapping("/pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@PathVariable("current") Integer current, @PathVariable("limit") Integer limit,
                                 @RequestBody(required = false) CourseQuery courseQuery){
        Page<EduCourse> pageCourse = new Page<>(current, limit);

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title", title);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status", status);
        }

        courseService.page(pageCourse, wrapper);
        long total = pageCourse.getTotal();
        List<EduCourse> courseList = pageCourse.getRecords();

        return R.ok().data("total", total).data("courseList", courseList);
    }

    @PostMapping("/addCourseInfo")
    @Transactional
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
       String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable("courseId") String courseId){
        CourseInfoVo course = courseService.getCourseInfo(courseId);
        return R.ok().data("course", course);
    }

    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    @GetMapping("/getPublishCourseInfo/{courseId}")
    public R getPublishCourseInfo(@PathVariable("courseId") String courseId){
        CoursePublishVo coursePublish = courseService.getPublishCourseInfo(courseId);
        return R.ok().data("coursePublish", coursePublish);
    }

    @PostMapping("/publishCourse/{courseId}")
    public R publishCourse(@PathVariable("courseId") String courseId){
        EduCourse course = new EduCourse();
        course.setId(courseId);
        course.setStatus("Normal");
        courseService.updateById(course);
        return R.ok();
    }

    @DeleteMapping("/deleteCourse/{courseId}")
    public R deleteCourse(@PathVariable("courseId") String courseId){
        courseService.removeCourseById(courseId);
        return R.ok();
    }

}

