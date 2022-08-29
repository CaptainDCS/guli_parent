package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;


    @PostMapping("/uploadAlYiVideo")
    public R uploadAlYiVideo(MultipartFile file){
        String videoId = vodService.uploadVideoAly(file);
        return R.ok().data("videoId", videoId);
    }

    @DeleteMapping("/removeAlYVideo/{videoId}")
    public R removeAlYVideo(@PathVariable("videoId") String videoId){
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);

            client.getAcsResponse(request);

            return R.ok();
        }catch (Exception e){
            e.printStackTrace();

            throw new GuliException(20001, "删除视频失败！");
        }

    }

    //删除多个视频
    @DeleteMapping("/deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList")List<String> videoIdList){
        vodService.removeMoreAlyVideo(videoIdList);
        return R.ok();
    }

    //根据视频的Id获取视频凭证
    @GetMapping("/getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable("id") String id){
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
            //获取凭证的request
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置视频ID
            request.setVideoId(id);
            //调用获取凭证的方法并传入request
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //获取凭证
            String playAuth = response.getPlayAuth();

            return R.ok().data("playAuth", playAuth);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "获取视频凭证失败");
        }
    }
}
