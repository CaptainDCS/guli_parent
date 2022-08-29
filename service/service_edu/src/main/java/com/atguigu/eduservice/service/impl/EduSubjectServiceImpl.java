package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author captainM
 * @since 2022-08-16
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {
        try {
            InputStream inputStream = file.getInputStream();
            /**
             * EasyExcel读取Excel文件的方法read()
             * InputStream inputStream -> 获取上传文件的输入流
             * Class head -> Excel文件的字段的映射实体类
             * ReadListener readListener -> 监听器，将读取到的数据进行插入数据库的操作
             */
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询所有的一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //查询所有的二级分分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //最终返回的一种特定格式的集合
        ArrayList<OneSubject> subjectList = new ArrayList<>();

        //封装所有的一级分类
        for (EduSubject eduOneSubject : oneSubjectList) {
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(eduOneSubject,oneSubject);
            subjectList.add(oneSubject);

            //封装所有的二级分类
            ArrayList<TwoSubject> twoSubjects = new ArrayList<>();

            for (EduSubject eduTwoSubject : twoSubjectList) {
                //如果二级分类的parent_id和一级分类的id值相同，则将对应的元素放入二级分类的封装对象中
                if(eduTwoSubject.getParentId().equals(eduOneSubject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(eduTwoSubject, twoSubject);
                    twoSubjects.add(twoSubject);
                }
            }
            //将封装好的二级对象再赋值给一级分类对象中
            oneSubject.setChildren(twoSubjects);
        }

        return subjectList;
    }
}
