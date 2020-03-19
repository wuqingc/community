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

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) throws IOException {

        AcessTokenDTO acessTokenDTO = new AcessTokenDTO();
        acessTokenDTO.setCode(code);
        acessTokenDTO.setClient_id(clientId);
        acessTokenDTO.setClient_secret(clientSecret);
        acessTokenDTO.setRedirect_uri(redirectUrl);
        acessTokenDTO.setState(state);
        String acessTokenKey = gitHubProvider.getAcessToken(acessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(acessTokenKey);
        if (gitHubUser != null) {
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(gitHubUser.getName());
            user.setAccount_id(String.valueOf(gitHubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_modified());
            userMapper.insert(user);
            /*
             * 登录成功,写Cookie和Session.
             * 重定向时,填写的路径为url拼接的路径(直接return的是templates/下的文件名).
             */
            HttpSession session = request.getSession();
            session.setAttribute("user",gitHubUser);
            return "redirect:/";
        } else {
            /*
             * 登录失败,重新登录.
             */
            return "redirect:/";
        }
    }
}
