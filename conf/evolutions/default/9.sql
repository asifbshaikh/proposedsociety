# --- !Ups

alter table form add column co_applicant_exists tinyint(1) default 0;

# --- !Downs
alter table form drop column co_applicant_exists;