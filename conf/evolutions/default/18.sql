# --- !Ups
create table attachment (
	id				bigint auto_increment not null,
	file_name		varchar(255),
	content_type 	varchar(100),
	upload_time		datetime,
	file_path		varchar(512) not null,
	user_id			bigint not null,
	constraint pk_attachment primary key(id)
);

alter table attachment add constraint fk_attachment__user_id foreign key(user_id) references user(id) on delete cascade on update cascade;

alter table applicant add column photograph_id bigint;

alter table applicant add constraint fk_applicant__photograph_id foreign key(photograph_id) references attachment(id) on delete set null on update set null;



alter table applicant add column identity1_id bigint;

alter table applicant add constraint fk_applicant__identity1_id foreign key(identity1_id) references attachment(id) on delete set null on update set null;

alter table applicant add column identity2_id bigint;

alter table applicant add constraint fk_applicant__identity2_id foreign key(identity2_id) references attachment(id) on delete set null on update set null;

alter table applicant add column identity3_id bigint;

alter table applicant add constraint fk_applicant__identity3_id foreign key(identity3_id) references attachment(id) on delete set null on update set null;

alter table applicant add column addressproof_id bigint;

alter table applicant add constraint fk_applicant__addressproof_id foreign key(addressproof_id) references attachment(id) on delete set null on update set null;

# --- !Downs
SET FOREIGN_KEY_CHECKS=0;

drop table if exists attachment;
alter table applicant drop foreign key fk_applicant__photograph_id;
alter table applicant drop column photograph_id;
alter table applicant drop foreign key fk_applicant__identity1_id;
alter table applicant drop column identity1_id;
alter table applicant drop foreign key fk_applicant__identity2_id;
alter table applicant drop column identity2_id;
alter table applicant drop foreign key fk_applicant__identity3_id;
alter table applicant drop column identity3_id;
alter table applicant drop foreign key fk_applicant__addressproof_id;
alter table applicant drop column addressproof_id;

SET FOREIGN_KEY_CHECKS=1;
