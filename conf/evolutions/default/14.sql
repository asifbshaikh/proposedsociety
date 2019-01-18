# --- !Ups
alter table applicant drop column nationality;

# --- !Downs
alter table applicant add column nationality varchar(50) not null;
