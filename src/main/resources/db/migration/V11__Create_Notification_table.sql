create table notification
(
	id bigint,
	notifier bigint not null,
	receiver bigint not null,
	outer_id bigint not null,
	type int not null,
	gmt_create bigint null,
	status int not null comment '已读/未读',
	constraint notification_pk
		primary key (id)
);