package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseVo;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.feign.OrderFeign;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/courseFront")
//@CrossOrigin
public class CourseFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private OrderFeign orderFeign;

    //前台所有课程的分页显示
    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R  getFrontCourseList(@PathVariable("page") long page, @PathVariable("limit") long limit,
                                 @RequestBody(required = false) CourseFrontVo courseFrontVo){

        Page<EduCourse> coursePage = new Page<>(page, limit);
        Map<String, Object> map = courseService.getCourseFrontList(coursePage, courseFrontVo);

        return R.ok().data(map);
    }

    //前台获取课程的详情
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable("courseId") String courseId, HttpServletRequest request){
        //根据课程ID查询课程的所有信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        //根据课程ID查询章节和小节信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        //根据课程ID获取订单状态
        String memberId = JwtUtils.getMemberIdByJwtToken(request);//获取用户ID
        //如果memberId为空证明没有登录则不需要判断是否购买
        if(StringUtils.isEmpty(memberId)){
            return R.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList);
        }
        //没有以上判断，不登陆直接报错！！！
        Boolean isBuy = orderFeign.isBuyCourse(courseId, memberId);//获取订单状态，false未支付true已支付

        return R.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList).data("isBuy", isBuy);
    }

    //根据课程ID查询课程信息
    @GetMapping("/getCourseInfoById/{courseId}")
    public CourseVo getCourseInfoById(@PathVariable("courseId") String courseId){

        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        CourseVo courseVo = new CourseVo();
        BeanUtils.copyProperties(courseWebVo, courseVo);

        return courseVo;
    }
}
