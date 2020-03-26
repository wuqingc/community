package com.lele.community.dto;

import com.lele.community.model.User;
import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
    private Long commentCount;
}
