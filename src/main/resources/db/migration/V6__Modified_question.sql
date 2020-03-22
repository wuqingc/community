alter table question change ID id int auto_increment;

alter table question change TITLE title varchar(50) null;

alter table question change DESCRIPTION description text null;

alter table question change GMT_CREATE gmt_create bigint null;

alter table question change GMT_MODIFIED gmt_modified bigint null;

alter table question change CREATEOT creator int null;

alter table question change COMMENT_COUNT comment_count int default 0 null;

alter table question change VIEW_COUNT view_count int default 0 null;

alter table question change LIKE_COUNT like_count int default 0 null;

alter table question change TAG tag varchar(256) null;

