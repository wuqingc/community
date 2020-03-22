alter table user change ID id int auto_increment;

alter table user change NAME name varchar(100) null;

alter table user change ACCOUNT_ID account_id varchar(30) null;

alter table user change TOKEN token char(36) null;

alter table user change GMT_CREATE gmt_create bigint null;

alter table user change GMT_MODIFIED gmt_modified bigint null;

alter table user change AVATAR_URL avatar_url varchar(100) null;

