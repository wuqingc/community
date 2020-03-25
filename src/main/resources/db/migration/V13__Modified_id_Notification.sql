alter table notification modify id bigint auto_increment;

create unique index notification_id_uindex
	on notification (id);