package com.lele.community.provider;

import com.alibaba.fastjson.JSON;
import com.lele.community.dto.AcessTokenDTO;
import com.lele.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {

    public String getAcessToken(AcessTokenDTO acessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        /*
         * 将DTO对象转成JSON.
         */
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(acessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            return str.split("&")[0].split("=")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GitHubUser getUser(String acessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+acessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            return JSON.parseObject(str,GitHubUser.class);
        }
    }

}
