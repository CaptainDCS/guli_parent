package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author captainM
 * @since 2022-08-17
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    public CoursePublishVo  getPublishCourseInfo(String courseId);

    CourseWebVo getBaseCourseInfo(String courseId);
}
