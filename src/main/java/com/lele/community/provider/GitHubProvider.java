package com.lele.community.provider;

import com.alibaba.fastjson.JSON;
import com.lele.community.dto.AcessTokenDTO;
import com.lele.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author lele
 * 一个github操作类,将其从Controller中抽离出来.
 */
@Component
public class GitHubProvider {

    /**
     * 通过OKHttp来模拟post请求,以此发送数据给github.
     * @param acessTokenDTO 数据传输对象,将信息封装在里面.
     * @return  拿到的tokenkey
     */
    public String getAcessToken(AcessTokenDTO acessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        /*
         * 利用fastJson工具将DTO对象转成JSON.
         */
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(acessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        /*
         * 解析字符串,拿到具体的token.
         */
        try (Response response = client.newCall(request).execute()) {
            String str = Objects.requireNonNull(response.body()).string();
            return str.split("&")[0].split("=")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 同样的利用okhttp来模拟请求.
     * @param acessToken 需要传的参数.
     * @return 返回一个json数据,将其封装成一个对象.
     * @throws IOException
     */
    public GitHubUser getUser(String acessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+acessToken)
                .build();
       /*
        * fastjson会自动读取到相应的属性注入到对象中.
        */
        try (Response response = client.newCall(request).execute()) {
            String str = Objects.requireNonNull(response.body()).string();
            return JSON.parseObject(str,GitHubUser.class);
        }
    }

}
