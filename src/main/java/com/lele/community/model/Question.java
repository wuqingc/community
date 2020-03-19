package com.lele.community.model;

import lombok.Data;

@Data
public class Question {
    private String title;
    private String description;
    private long gmt_create;
    private long gmt_modified;
    private long creator;
    private long comment_count;
    private long view_count;
    private long like_count;
    private String tag;
}
