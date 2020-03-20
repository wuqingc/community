package com.lele.community.controller;

import com.lele.community.dto.PaginationDTO;
import com.lele.community.dto.QuestionDTO;
import com.lele.community.mapper.QuestionMapper;
import com.lele.community.mapper.UserMapper;
import com.lele.community.model.Question;
import com.lele.community.model.User;
import com.lele.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    /**
     * 主页面的请求
     * @param request
     * @param model
     * @param page 分页的请求页
     * @param size 每页的数据
     * @return 写好的html页面
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size){
        /*
         * 取出Cookie中的token,利用token来判断数据库中是否该记录.
         * 如果存在就直接在session上绑定用户信息.
         */
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    User user = userMapper.findToken(cookie.getValue());
                    if (user != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        /*
         * 实现查找记录,并将查找到的记录封装成一个对象传递给前台.
         */
        PaginationDTO pagination = questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
