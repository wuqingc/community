alter table notification
	add notifier_name varchar(100) null after notifier;

alter table notification modify receiver bigint not null after status;

alter table notification
	add outer_title varchar(256) null after outer_id;

alter table notification modify type int not null after receiver;

