# --- !Ups

create table persistent_id (
  id                      bigint auto_increment not null,
  name                    varchar(100) not null,
  key_value               bigint not null,
  constraint pk_persistent_id primary key (id)
);

alter table persistent_id add constraint uk_persistent_id_name unique key (name);
insert into persistent_id (name, key_value) values ('receiptId','501');
insert into persistent_id (name, key_value) values ('formNumber','5001');
alter table invoice add column receipt_id bigint;


# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;

alter table invoice drop column receipt_id;
drop table if exists persistent_id;

SET FOREIGN_KEY_CHECKS = 1;
