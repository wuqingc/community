alter table comment modify commentator bigint null comment '评论人id';
alter table question modify creator bigint null;