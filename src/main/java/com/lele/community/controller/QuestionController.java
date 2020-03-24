package com.lele.community.controller;

import com.lele.community.dto.CommentCreateDTO;
import com.lele.community.dto.QuestionDTO;
import com.lele.community.enums.CommentTypeEnum;
import com.lele.community.service.CommentService;
import com.lele.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model){

        List<CommentCreateDTO> commentCreateDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("commentCreateDTOS",commentCreateDTOS);

        questionService.inView(id);
        QuestionDTO questionDTO = questionService.listByQuestionId(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
