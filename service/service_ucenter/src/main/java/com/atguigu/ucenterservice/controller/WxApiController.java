package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.ConstantPropertiesUtil;
import com.atguigu.ucenterservice.utils.HttpClientUtils;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller //注意这里没有配置 @RestController
//@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    @GetMapping("/callback")
    public String callback(String code, String state){

        try {
            //拿着code，去请求微信固定的地址，得到两个值 access_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token"+
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_APP_ID,
                    ConstantPropertiesUtil.WX_APP_SECRET,
                    code
            );

            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //使用Gson工具解析得到的JSON字符串获取openid和access_token
            Gson gson = new Gson();
            HashMap<String, String> map = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = map.get("access_token");
            String openid = map.get("openid");

            //判断数据库中是否有微信用户的信息，如果没有就添加到数据库
            UcenterMember member = memberService.getOpenIdMember(openid);
            if(member == null){
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl  = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                //获取微信用户的用户信息
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap<String, String> userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = userInfoMap.get("nickname");
                String headimgurl = userInfoMap.get("headimgurl");

                member = new UcenterMember();
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                member.setNickname(nickname);
                memberService.save(member);
            }

            //使用jwt生成一个ucenterMember的token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            //重定向到前端主页
            return "redirect:http://localhost:3000?token="+jwtToken;

        } catch (Exception e) {
            throw new GuliException(20001, "登录失败！");
        }
    }

    //生成微信扫描二维码,   %s 相当于?占位符
    @GetMapping("/login")
    public String getWxCode(){
        //微信登录的第一步：获取开放社区的redirect_uri和appid
        String baseUrl ="https://open.weixin.qq.com/connect/qrconnect"
                +"?appid=%s"
                +"&redirect_uri=%s"
                +"&response_type=code"
                +"&scope=snsapi_login"
                +"&state=%s"
                +"#wechat_redirect";
        //对redirect_url进行编码
        String redirect_url = ConstantPropertiesUtil.WX_APP_ID;
        try {
            redirect_url = URLEncoder.encode(redirect_url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置 %s 占位符的参数，上面有3处
        String url = String.format(
                baseUrl,
                redirect_url,
                ConstantPropertiesUtil.WX_REDIRECT_URL,
                "atguigu"
        );

        return "redirect:"+url;
    }

}
