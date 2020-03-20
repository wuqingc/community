package com.lele.community.controller;

import com.lele.community.mapper.QuestionMapper;
import com.lele.community.mapper.UserMapper;
import com.lele.community.model.Question;
import com.lele.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author xuan
 * 发布页面的Controller,利用RESRful形式来开发代码.
 */
@Controller()
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    private String path = "publish";
    private String errorInfo = "error";


    @GetMapping("/publish")
    public String publish(){
        return path;
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(name = "title") String title,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "tag") String tag,
                            HttpServletRequest request,
                            Model model
                            ){
        /*
         * 失败后可以回显正确的数据.
         */
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        /*
         * 错误情形处理.
         */
        if (title == null || "".equals(title)){
            model.addAttribute(errorInfo,"标题不能为空.");
            return path;
        }
        if (description == null || "".equals(description)){
            model.addAttribute(errorInfo,"描述不能为空.");
            return path;
        }
        if (tag == null || "".equals(tag)){
            model.addAttribute(errorInfo,"标签不能为空.");
            return path;
        }


        /*
         * 如果用户没有登录就进行操作,直接返回错误信息.
         */
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    User user = userMapper.findToken(cookie.getValue());
                    if (user != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);

                        /*
                         * 正常操作:将数据封装成一个实体类Question插入数据库.
                         */
                        Question question = new Question();
                        question.setTitle(title);
                        question.setDescription(description);
                        question.setTag(tag);
                        question.setCreator(user.getId());
                        question.setGmt_create(System.currentTimeMillis());
                        question.setGmt_modified(question.getGmt_create());
                        questionMapper.create(question);
                    } else {
                        model.addAttribute(errorInfo, "用户未登录.");
                        return path;
                    }
                    break;
                }
            }
        }

        return "redirect:/";
    }
}
