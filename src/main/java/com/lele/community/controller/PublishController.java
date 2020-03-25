package com.lele.community.controller;

import com.alibaba.fastjson.JSON;
import com.lele.community.cache.TagCache;
import com.lele.community.dto.QuestionDTO;
import com.lele.community.mapper.QuestionMapper;
import com.lele.community.mapper.UserMapper;
import com.lele.community.model.Question;
import com.lele.community.model.User;
import com.lele.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuan
 * 发布页面的Controller,利用RESRful形式来开发代码.
 */
@Controller()
public class PublishController {

    @Autowired
    private QuestionService questionService;

    private String path = "publish";
    private String errorInfo = "error";


    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("selectTags",TagCache.get());
        return path;
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){
        QuestionDTO question = questionService.listByQuestionId(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",id);
        return "publish";
    }

    @PostMapping("/publish/verify")
    @ResponseBody
    public Object verify(@RequestBody QuestionDTO questionDTO,
                         HttpServletRequest request
                         ){
        String title = questionDTO.getTitle();
        String description = questionDTO.getDescription();
        String tag = questionDTO.getTag();

        User user = (User) request.getSession().getAttribute("user");
        Map<String,String> errors = new HashMap<>();
        if (user == null) {
            errors.put(errorInfo, "用户未登录.");
            return errors;
        }
        if (title == null || "".equals(title)){
            errors.put(errorInfo,"标题不能为空.");
            return errors;
        }
        if (description == null || "".equals(description)){
            errors.put(errorInfo,"描述不能为空.");
            return errors;
        }
        if (tag == null || "".equals(tag)){
            errors.put(errorInfo,"标签不能为空.");
            return errors;
        }
        if (!TagCache.isVaild(tag)) {
            errors.put(errorInfo,"非法标签,不能自定义.");
        }
        return errors;
    }


    @PostMapping("/publish")
    public String doPublish(@RequestParam(name = "title") String title,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "tag") String tag,
                            @RequestParam(name = "id",defaultValue = "0") String id,
                            HttpServletRequest request,
                            Model model
                            ){

        model.addAttribute("selectTags",TagCache.get());
        /*
         * 失败后可以回显正确的数据.
         */
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        /*
         * 错误情形处理,在前端处理即可.
         */
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute(errorInfo, "用户未登录.");
            return path;
        }

        if (StringUtils.isBlank(title) || StringUtils.isBlank(description) || StringUtils.isBlank(tag)
                || !TagCache.isVaild(tag)){
            return path;
        }

        /*
         * 正常操作:将数据封装成一个实体类Question插入数据库.
         */
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setId(Long.valueOf(id));

        questionService.updateOrInsert(question);
        return "redirect:/";
    }
}
