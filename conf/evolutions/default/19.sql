# --- !Ups

create table payment_info(
	id 				bigint auto_increment not null,
	payment_mode	varchar(100) not null,
	bank_name		varchar(100) not null,
	branch_name		varchar(100) not null,
	payment_date 	datetime 	not null,
	cheque_number	bigint not null,
	transaction_id	varchar(100) not null,
	account_number	varchar(100) not null,
	receipt_number	varchar(100) not null,
	form_id    bigint ,
	constraint pk_payment_info primary key (id)
);

create table payment_code(
	id 				    bigint auto_increment not null,
	code                varchar(50) not null,
	name                varchar(100) not null,
	email               varchar(100) not null,
	mobile              varchar(50) not null,
	amount              bigint not null,
	generation_time 	datetime not null,
	invalidate_time 	datetime,
	used			    tinyint(1) default 0,
	form_id             bigint,
	constraint pk_payment_code primary key (id)
);

create table staff_user(
	id 				    bigint auto_increment not null,
	name                varchar(100) not null,
	email                varchar(100) not null,
	role                varchar(50) not null,
	password            varchar(255),
	user_id				varchar(50) not null,
	last_login			datetime,
	last_login_ip		varchar(20),
	constraint pk_staff_user primary key (id)	
);

alter table payment_info add constraint fk_form_id  foreign key (form_id) references form(id) on delete cascade on update cascade;
alter table payment_code add constraint fk_payment_code__form_id foreign key (form_id) references form(id) on delete set null on update set null;
alter table payment_code add constraint uk_payment_code__code unique index(code);

alter table staff_user add constraint uk_staff_user__user_id unique index(user_id);

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;
drop table payment_info;
drop table payment_code;
drop table staff_user;
SET FOREIGN_KEY_CHECKS=1;