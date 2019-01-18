# --- !Ups
alter table payment drop column amount;

create table cash_with_you (
  id                        bigint auto_increment not null,
  cash_with_you                   bigint not null,
  form_id                   bigint not null,
  constraint pk_cash_with_you primary key (id)
);

# cash_with_you table constraints
alter table cash_with_you add constraint fk_cash_with_you__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;

# --- !Downs
alter table payment add column amount decimal(15, 2) NOT NULL;
alter table cash_with_you drop foreign key fk_cash_with_you__form_id;
drop table cash_with_you;