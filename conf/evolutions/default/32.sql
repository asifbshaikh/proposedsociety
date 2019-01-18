# --- !Ups
alter table agent_form add column form_filled_year bigint;

create table agent_feedback_form_file (
	id bigint auto_increment not null,
	upload_time datetime not null,
	feedback_form_file_id bigint not null,
	constraint pk_agent_feedback_form_file primary key(id)
);

alter table agent_feedback_form_file add constraint fk_agent_feedback_form_file__feedback_form_file_id foreign key(feedback_form_file_id) references attachment(id);

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;
alter table agent_form drop column form_filled_year;
drop table agent_feedback_form_file;

SET FOREIGN_KEY_CHECKS=1;