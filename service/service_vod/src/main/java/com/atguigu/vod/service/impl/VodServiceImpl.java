package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.UploadAttachedMedia;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService  {
    @Override
    public String uploadVideoAly(MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();

            //初始化对象
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET, title, fileName, inputStream);
            //创建删除视频的的业务层对象
            UploadVideoImpl uploadVideo = new UploadVideoImpl();
            //调用以流的方式上传的方法，完成上传并返回消息
            UploadStreamResponse response = uploadVideo.uploadStream(request);

            String videoId = null;

            if(response.isSuccess()){
                //从相应消息中获取视频的ID
                videoId = response.getVideoId();
            } else {
                videoId = response.getVideoId();
            }

            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public void removeMoreAlyVideo(List<String> videoIdList) {
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");
            request.setVideoIds(videoIds);

            client.getAcsResponse(request);

        }catch (Exception e){
            e.printStackTrace();

            throw new GuliException(20001, "删除视频失败！");
        }

    }
}
