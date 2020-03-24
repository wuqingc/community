package com.lele.community.controller;

import com.lele.community.dto.CommentDTO;
import com.lele.community.dto.ResultDTO;
import com.lele.community.exception.CustomizeErrorCode;
import com.lele.community.model.Comment;
import com.lele.community.model.User;
import com.lele.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping("/comment")
    @ResponseBody
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO,comment);
        comment.setCommentator(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        commentService.insert(comment);

        return ResultDTO.okOf();
    }
}
