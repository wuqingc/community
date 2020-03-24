package com.lele.community.service;

import com.lele.community.enums.CommentTypeEnum;
import com.lele.community.exception.CustomizeErrorCode;
import com.lele.community.exception.CustomizeException;
import com.lele.community.mapper.CommentMapper;
import com.lele.community.mapper.QuestionExtMapper;
import com.lele.community.mapper.QuestionMapper;
import com.lele.community.model.Comment;
import com.lele.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 新增评论时需要进行一些验证.
     * @param comment
     */
    @Transactional
    public void insert(Comment comment) {
        /*
         * 当评论的绑定值不存在时,需要抛出异常,不能插入.
         */
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_PARAM);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExit(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_NOT_WRONG);
        }

        /*
         * 判断是问题回复还是评论回复.
         */
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        } else {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            /*
             * 问题更新评论数.
             */
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
}
