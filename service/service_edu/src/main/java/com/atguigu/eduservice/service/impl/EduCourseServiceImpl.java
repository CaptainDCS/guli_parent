package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.*;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author captainM
 * @since 2022-08-17
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;
//    @Autowired
//    private EduSubjectService subjectService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduVideoService videoService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //将课程基本信息添加到课程表edu_course中
        //创建一个EduCourse对象用于存储课程的基本信息
        EduCourse eduCourse = new EduCourse();
        //将表单中的基本信息赋值给EduCourse对象
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        //获取课程分类id，根据id获取一级分类id,并传入EduCourse对象
//        String subjectId = courseInfoVo.getSubjectId();
//        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
//        wrapper.eq("id", subjectId);
//        EduSubject subject = subjectService.getOne(wrapper);
//        eduCourse.setSubjectParentId(subject.getParentId());

        int row = baseMapper.insert(eduCourse);

        if(row == 0){
            //如果添加失败则抛出异常
            throw new GuliException(20001, "课程信息添加失败");
        }

        //获取课程表中id赋值给课程简介id，使两表数据保持一对一的关系
        String courseId = eduCourse.getId();
        //将课程简介添加到课程简介表edu_course_description中
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, eduCourseDescription);
        eduCourseDescription.setId(courseId);
        courseDescriptionService.save(eduCourseDescription);

        return courseId;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        //创建CourseInfoVo实例用于存储查询到的相关数据
        CourseInfoVo courseInfoVo = new CourseInfoVo();

        //根据课程ID查询课程表相关信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse, courseInfoVo);
        //根据课程ID查询课程简介表相关信息
        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(courseId);
        BeanUtils.copyProperties(eduCourseDescription, courseInfoVo);

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //创建EduCourse实例，用于存储表单中传来的数据
        EduCourse eduCourse = new EduCourse();

        EduCourseDescription eduCourseDescription = new EduCourseDescription();

        //修改课程信息表
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int row = baseMapper.updateById(eduCourse);
        if(row == 0){
            throw new GuliException(20001, "修改课程信息失败");
        }

        //修改课程简介信息
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(eduCourseDescription);
    }

    @Override
    public CoursePublishVo getPublishCourseInfo(String courseId) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(courseId);
        return publishCourseInfo;
    }

    @Override
    public void removeCourseById(String courseId) {
        //根据课程ID删除小节
        videoService.removeVideoByCourseId(courseId);
        //根据课程ID删除章节
        chapterService.removeChapterByCourseId(courseId);
        //根据课程ID删除课程描述
        courseDescriptionService.removeById(courseId);
        //根据课程ID删除课程
        int row = baseMapper.deleteById(courseId);
        if(row == 0){
            throw new GuliException(20001, "删除失败！");
        }
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        //条件拼接
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){
            courseQueryWrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());//一级分类ID
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){
            courseQueryWrapper.eq("subject_id", courseFrontVo.getSubjectId());//二级分类ID
        }
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            courseQueryWrapper.orderByDesc("buy_count");//根据购买数降序排列
        }
        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){
            courseQueryWrapper.orderByDesc("gmt_create");//根据创建时间降序排列
        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){
            courseQueryWrapper.orderByDesc("price");//根据价格降序排列
        }
        //分页操作
        baseMapper.selectPage(coursePage, courseQueryWrapper);
        //获取分页信息
        List<EduCourse> records = coursePage.getRecords();
        long pages = coursePage.getPages();
        long size = coursePage.getSize();
        long total = coursePage.getTotal();
        long current = coursePage.getCurrent();
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();

        //将分页信息封装在map集合中返回给前端
        HashMap<String, Object> map = new HashMap<>();
        map.put("pages", pages);
        map.put("items", records);
        map.put("current", current);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }


    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {

        return baseMapper.getBaseCourseInfo(courseId);

    }
}
