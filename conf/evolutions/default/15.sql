# --- !Ups
alter table payment drop column processed;
alter table payment add column client_processed tinyint(1) default 0;
alter table payment add column gateway_processed tinyint(1) default 0;
alter table payment add column success tinyint(1) default 0;

# --- !Downs
alter table applicant add column nationality varchar(50) not null;
alter table payment add column processed tinyint(1) default 0;
alter table payment drop column client_processed;
alter table payment drop column gateway_processed;
alter table payment drop column success;
