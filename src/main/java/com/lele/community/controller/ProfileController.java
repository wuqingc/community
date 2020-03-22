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

import javax.servlet.http.HttpServletRequest;


@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    /**
     *
     * @param action 占位符写法,可以在下面直接拿到.
     * @param model
     * @param request
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "3") Integer size){

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:/";
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