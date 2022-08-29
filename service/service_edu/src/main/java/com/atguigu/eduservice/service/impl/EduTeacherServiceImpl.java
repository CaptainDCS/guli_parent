package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author captainM
 * @since 2022-08-12
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //前端讲师页面分页查询
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> eduTeacherPage) {
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByDesc("level");//通过职称进行降序排列

        //分页查询
        baseMapper.selectPage(eduTeacherPage,teacherQueryWrapper);
        //获取分页后的所有信息
        List<EduTeacher> records = eduTeacherPage.getRecords();
        long current = eduTeacherPage.getCurrent();
        long pages = eduTeacherPage.getPages();
        long size = eduTeacherPage.getSize();
        long total = eduTeacherPage.getTotal();

        boolean hasNext = eduTeacherPage.hasNext();//当前页是否有下一页
        boolean hasPrevious = eduTeacherPage.hasPrevious();//当前页是否有上一页

        //将分页信息全部存入map集合并返回
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("pages", pages);
        map.put("current", current);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    //根据职称获取排名前四的最牛逼老师
    @Cacheable(key = "'selectPopularTeacherList'", value = "teachers")
    @Override
    public List<EduTeacher> PopularTeacher() {
        QueryWrapper<EduTeacher> eduTeacherQueryWrapper = new QueryWrapper<>();
        eduTeacherQueryWrapper.orderByDesc("level").last("limit 4");
        List<EduTeacher> teacherList = baseMapper.selectList(eduTeacherQueryWrapper);
        return teacherList;
    }

}
