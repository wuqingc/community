package com.lele.community.service;

import com.lele.community.dto.CommentCreateDTO;
import com.lele.community.enums.CommentTypeEnum;
import com.lele.community.enums.NotificationTypeEnum;
import com.lele.community.enums.NotificationStatusEnum;
import com.lele.community.exception.CustomizeErrorCode;
import com.lele.community.exception.CustomizeException;
import com.lele.community.mapper.*;
import com.lele.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 新增评论时需要进行一些验证.
     * @param comment
     */
    @Transactional
    public void insert(Comment comment,User commentator) {
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
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            /*
             * 进行通知.
             */
            createNotify(comment, dbComment.getCommentator(), commentator.getName(),question.getTitle(), NotificationTypeEnum.RERPLY_COMMENT,question.getId());
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

            createNotify(comment,question.getCreator(), commentator.getName(),question.getTitle(), NotificationTypeEnum.REPLY_QUESTION,question.getId());
        }
    }

    /**
     * 创建通知.
     */
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum rerplyComment,Long outerId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(rerplyComment.getType());
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setOuterId(outerId);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentCreateDTO> listByTargetId(Long id, CommentTypeEnum commentTypeEnum) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id)
                .andTypeEqualTo(commentTypeEnum.getType());
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        comments.sort((o1,o2)-> (int) (o2.getGmtCreate() - o1.getGmtCreate()));
        if (comments.isEmpty()) {
            return new ArrayList<>();
        }

        /*
         * 获取评论人.
         */
        Set<Long> commentators = comments.stream().map(Comment::getCommentator).collect(Collectors.toSet());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(new ArrayList<>(commentators));
        List<User> users = userMapper.selectByExample(userExample);

        Map<Long,User> userMap = users.stream().collect(
                Collectors.toMap(User::getId, user -> user)
        );

        /*
         * 将评论人与Comment记录联系起来,封装成一个记录返回.
         */

        return comments.stream().map(comment -> {
            CommentCreateDTO commentCreateDTO = new CommentCreateDTO();
            BeanUtils.copyProperties(comment,commentCreateDTO);
            commentCreateDTO.setUser(userMap.get(commentCreateDTO.getCommentator()));
            return commentCreateDTO;
        }).collect(Collectors.toList());
    }
}
