package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.feign.VodFeign;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author captainM
 * @since 2022-08-17
 */
@RestController
@RequestMapping("/eduservice/edu-video")
@Transactional
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;
    @Autowired
    private VodFeign vodFeign;

    @PostMapping("/addVideo")
    public R  addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    @GetMapping("/getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable("videoId") String videoId){
        EduVideo video = videoService.getById(videoId);
        return R.ok().data("video", video);
    }

    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){

        return R.ok();
    }

    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable("videoId") String videoId){
        //根据小节ID获取视频ID，
        EduVideo eduVideo = videoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        //如果小节中有视频则删除，没有则不删除
        if(!StringUtils.isEmpty(videoSourceId)){
            R row = vodFeign.removeAlYVideo(videoSourceId);

            if(row.getCode() == 20001){
                throw new GuliException(20001, "视频服务异常，请稍后再试");
            }
        }

        //删除小节
        videoService.removeById(videoId);
        return R.ok();
    }
}

