package com.lele.community.controller;

import com.lele.community.dto.AcessTokenDTO;
import com.lele.community.dto.GitHubUser;
import com.lele.community.mapper.UserMapper;
import com.lele.community.model.User;
import com.lele.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.url}")
    private String redirectUrl;

    /**
     * github回调的路径.
     * @param code 传递过来一个code,用作下次验证
     * @param state 之前请求的那个state
     * @param request
     * @param response
     * @return 重定向回主页面.
     * @throws IOException
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {

        AcessTokenDTO acessTokenDTO = new AcessTokenDTO();
        acessTokenDTO.setCode(code);
        acessTokenDTO.setClient_id(clientId);
        acessTokenDTO.setClient_secret(clientSecret);
        acessTokenDTO.setRedirect_uri(redirectUrl);
        acessTokenDTO.setState(state);

        /*
         * 1.携带相关的信息继续向github发起请求,拿到token-key.
         * 2.携带token-key发起请求,获得数据对象.
         */
        String acessTokenKey = gitHubProvider.getAcessToken(acessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(acessTokenKey);


        /*
         * 登录成功之后,将该用户信息插入数据库中,并绑定一个随机生成的token值.
         * 重定向时,填写的路径为url拼接的路径(直接return的是templates/下的文件名).
         */
        if (gitHubUser != null) {
            User user = new User();
            /*
             * 利用UUID来随机生成一个token.
             */
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccount_id(String.valueOf(gitHubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_modified());
            user.setAvatar_url(gitHubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
        }
        return "redirect:/";
    }
}
