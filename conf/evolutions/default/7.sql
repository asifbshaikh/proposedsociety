# --- !Ups

alter table requirement add column form_id bigint not null;

# requirement table constraints
alter table requirement add constraint fk_requirement__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;

# --- !Downs
alter table requirement drop foreign key fk_requirement__form_id;
alter table requirement drop column form_id;