package com.lele.community.dto;

import com.lele.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private int id;
    private String title;
    private String description;
    private long gmtCreate;
    private long gmtModified;
    private int creator;
    private long commentCount;
    private long viewCount;
    private long likeCount;
    private String tag;

    private User user;
}
