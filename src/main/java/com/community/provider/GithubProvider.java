package com.community.provider;

import com.alibaba.fastjson.JSON;
import com.community.dto.AccessTokenDTO;
import com.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();


        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token?client_id=6e4b1ed26688d8072b88&client_secret=40bacfe49ff68ef2a2a559df7aa6eb51eb65c596&code="+accessTokenDTO.getCode()+"&redirect_uri=http://localhost:8887/callback&state=1")
                //.url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string=response.body().string();
            String token= string.split("&")[0].split("=")[1];
            System.out.println(string);
            System.out.println(token);
            return token;

        } catch (Exception e) {
         e.printStackTrace();
        }
        return null;
    }
    public GithubUser githubUser(String accessToken)
    {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url("https://api.github.com/user?access_token="+accessToken).build();
        try {
            Response response=client.newCall(request).execute();
            String string=response.body().string();
            GithubUser githubUser=JSON.parseObject(string,GithubUser.class);
            return githubUser;

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;



    }

}


