create table comment
(
    id bigint auto_increment primary key,
    parent_id bigint null comment '父类id',
    type int null comment '区分一级回复和二级回复',
    commentator int null comment '评论人id',
    gmt_create bigint not null comment '创建时间',
    gmt_modified bigint not null comment '更新时间',
    like_count bigint default 0 null comment '点赞数'
);

create unique index comment_id_uindex
    on comment (id);