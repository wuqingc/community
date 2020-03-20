package com.lele.community.controller;

import com.lele.community.dto.PaginationDTO;
import com.lele.community.mapper.UserMapper;
import com.lele.community.model.User;
import com.lele.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "3") Integer size){

        User user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    user = userMapper.findToken(cookie.getValue());
                    if (user != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);
                    } else {
                        return "redirect:/";
                    }
                    break;
                }
            }
        }

        if ("question".equals(action)) {
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
        } else if ("replies".equals(action)) {
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        PaginationDTO paginationDTO = questionService.list(user.getId(),page,size);
        model.addAttribute("pagination",paginationDTO);

        return "profile";
    }
}
