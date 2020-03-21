package com.lele.community.dto;

import com.lele.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private int id;
    private String title;
    private String description;
    private long gmt_create;
    private long gmt_modified;
    private long creator;
    private long comment_count;
    private long view_count;
    private long like_count;
    private String tag;

    private User user;
}
