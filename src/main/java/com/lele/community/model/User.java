package com.lele.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String account_id;
    private String token;
    private long gmt_create;
    private long gmt_modified;
    private String avatar_url;
}
