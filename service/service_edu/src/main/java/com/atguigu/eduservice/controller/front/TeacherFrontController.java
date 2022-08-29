package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/teacherFront")
//@CrossOrigin
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;

    @PostMapping("/getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(@PathVariable("page") long page, @PathVariable("limit") long limit){
        Page<EduTeacher> eduTeacherPage = new Page<>(page, limit);
        Map<String, Object>map = teacherService.getTeacherFrontList(eduTeacherPage);
        return R.ok().data(map);
    }

    //前端讲师页面讲师详情
    @GetMapping("/getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable("teacherId") String teacherId){
        //根据讲师id查询讲师基本信息
        EduTeacher teacher = teacherService.getById(teacherId);
        //根据讲师id查询对应讲师的所有课程
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("teacher_id", teacherId);
        List<EduCourse> courseList = courseService.list(courseQueryWrapper);


        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }

}
