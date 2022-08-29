package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.feign.VodFeign;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodFeign {
    @Override
    public R removeAlYVideo(String videoId) {
        return R.error().message("删除视频失败！服务器被透了。");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频失败！服务器被透了。");
    }
}
