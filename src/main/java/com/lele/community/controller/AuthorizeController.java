package com.lele.community.controller;

import com.lele.community.dto.AcessTokenDTO;
import com.lele.community.dto.GitHubUser;
import com.lele.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) throws IOException {

        AcessTokenDTO acessTokenDTO = new AcessTokenDTO();
        acessTokenDTO.setCode(code);
        acessTokenDTO.setClient_id("382e5710bc068c24c967");
        acessTokenDTO.setClient_secret("c47ab813273c74ff9616a17f9b8032c5cc4d1868");
        acessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        acessTokenDTO.setState(state);
        String acessTokenKey = gitHubProvider.getAcessToken(acessTokenDTO);

        GitHubUser user = gitHubProvider.getUser(acessTokenKey);
        System.out.println(user.getName());
        return "index";
    }
}
