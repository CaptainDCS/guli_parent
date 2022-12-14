package com.atguigu.eduservice.entity.vo;

import com.atguigu.eduservice.entity.EduVideo;
import lombok.Data;

import java.util.List;

@Data
public class ChapterVo {

    private String id;

    private String title;

    private List<VideoVo> children;
}
