package com.lele.community.controller;

import com.lele.community.mapper.UserMapper;
import com.lele.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        /*
         * 利用token来判断数据库中是否该记录.
         * 如果存在就直接绑定用户信息.
         */
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
            User user = userMapper.findToken(token);
            if (user != null) {
                /*
                 * 登录成功,写Cookie和Session.
                 * 重定向时,填写的路径为url拼接的路径(直接return的是templates/下的文件名).
                 */
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
            }
        }
        return "index";
    }
}
