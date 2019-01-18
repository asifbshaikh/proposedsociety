# --- !Ups
alter table form modify form_number varchar(50);
alter table form modify status varchar(50);

alter table applicant modify phone1 varchar(30) not null;
alter table applicant modify phone2 varchar(30);
alter table applicant modify phone3 varchar(30);
alter table applicant modify off_phone1 varchar(30) not null;
alter table applicant modify off_phone2 varchar(30);
alter table applicant modify off_phone3 varchar(30);

alter table user add column last_login_ip varchar(20);

# --- !Downs

alter table form modify form_number varchar(50) not null;
alter table form modify status varchar(50) not null;

alter table applicant modify phone1 varchar(20) not null;
alter table applicant modify phone2 varchar(20);
alter table applicant modify phone3 varchar(20);
alter table applicant modify off_phone1 varchar(20) not null;
alter table applicant modify off_phone2 varchar(20);
alter table applicant modify off_phone3 varchar(20);

alter table user drop column last_login_ip;