package com.lele.community.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long parentId;
    private String Content;
    private Integer type;
}
