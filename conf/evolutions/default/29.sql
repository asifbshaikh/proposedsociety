# --- !Ups

alter table user
add column interested_in_agent tinyint(1);
alter table form add column applicant_income_short_form bigint;
alter table form add column co_applicant_income_short_form bigint;
alter table form add column required_loan bigint;
alter table form add column total_budget bigint;
alter table form add column agent_id bigint;
alter table form add column short_form_applicant_income_per varchar(255);
alter table form add column short_form_co_applicant_income_per varchar(255);
alter table form add column registration_by varchar(255);

alter table applicant modify column sex char(1) null;
alter table applicant modify column fh_title varchar(255) null;
alter table applicant modify column fh_fname varchar(255) null;
alter table applicant modify column fh_mname varchar(255) null;
alter table applicant modify column fh_relation varchar(255) null;
alter table applicant modify column marital_status char(1) null;
alter table applicant modify column phone1_type varchar(255) null;
drop table if exists short_requirement_address; 
drop table if exists short_form;
alter table form add constraint fk_form__agent_id foreign key (agent_id) references agent(id) on delete set null on update set null;

# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;

alter table user

drop column interested_in_agent;
alter table form drop foreign key fk_form__agent_id;
alter table applicant modify column phone1_type varchar(255) not null;
alter table applicant modify column marital_status char(1) not null;
alter table applicant modify column fh_relation varchar(255) not null;
alter table applicant modify column fh_mname varchar(255) not null;
alter table applicant modify column fh_fname varchar(255) not null;
alter table applicant modify column fh_title varchar(255) not null;
alter table applicant modify column sex char(1) not null;

alter table form drop column registration_by;
alter table form drop column short_form_co_applicant_income_per;
alter table form drop column short_form_applicant_income_per;
alter table form drop column agent_id ;
alter table form drop column total_budget;
alter table form drop column required_loan;
alter table form drop column co_applicant_income_short_form;
alter table form drop column applicant_income_short_form;

SET FOREIGN_KEY_CHECKS = 1;