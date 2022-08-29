package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    /**
     * 因为该监听器类没有注入到Spring容器中，所以执行写入数据库的操作需要引入subjectService对象进行操作
     * 通过有参构造将subjectService传入，执行相应的添加操作
     */
    private EduSubjectService subjectService;

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        //判断文件是否有内容
        if(subjectData == null){
            throw new GuliException(20001, "文件数据为空");
        }

        //一行一行读取数据，需要排除表格数据中的重复列
        EduSubject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());

        //没有相同的一级分类，将相应的数据存入数据库
        if(existOneSubject == null){
            existOneSubject = new EduSubject();
            //一级分类的parent_id值为0
            existOneSubject.setParentId("0");
            //写入Excel表格中第一列的数据
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(existOneSubject);
        }

        //获取一级分类的ID值，作为二级分类的parent_id值
        String pid = existOneSubject.getId();
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        //没有相同的二级分类，将相应的数据存入数据库
        if(existTwoSubject == null){
            existTwoSubject = new EduSubject();
            //二级分类的parent_id值为一级分类的id值
            existTwoSubject.setParentId(pid);
            //写入Excel表格中第二列的数据
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(existTwoSubject);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService, String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService, String name, String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

}
