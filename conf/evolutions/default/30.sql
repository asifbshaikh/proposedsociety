# --- !Ups

create table query (
  id                        bigint auto_increment not null,
  user_id              	    bigint not null,
  query_text                text,
  time_stamp                datetime not null,
  constraint pk_user_query primary key (id)
);

alter table payment add column bank_name varchar(50);
alter table payment add column nb_bid varchar(50);
alter table payment add column nb_order_no varchar(50);


alter table query add constraint fk_query_user foreign key(user_id) references user(id);

# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;

alter table payment drop column bank_name;
alter table payment drop column nb_bid;
alter table payment drop column nb_order_no;
drop table if exists query;

SET FOREIGN_KEY_CHECKS = 1;
