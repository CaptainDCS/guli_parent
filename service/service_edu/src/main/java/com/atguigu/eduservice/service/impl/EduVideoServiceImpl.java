package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.feign.VodFeign;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author captainM
 * @since 2022-08-17
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VodFeign vodFeign;

    @Override
    public void removeVideoByCourseId(String courseId) {
        //通过课程删除视频
        QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.select("video_source_id").eq("course_id", courseId);
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapper1);

        //获取所有的视频id转换为String类型的集合
        List<String> videoIds = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                videoIds.add(videoSourceId);
            }
        }

        //远程调用vod中批量删除视频的方法
        if(videoIds.size() > 0){
            R row = vodFeign.deleteBatch(videoIds);
            if(row.getCode() == 20001){
                throw new GuliException(20001, "视频服务异常，请稍后再试");
            }
        }

        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
