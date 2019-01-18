# --- !Ups

alter table user drop column role;
alter table user add column reset_password varchar(100) ;
alter table user add constraint uk_user_reset_password unique key (reset_password);
alter table user modify column mobile varchar(255);
alter table user modify column email varchar(255);
alter table user modify column joining_date datetime;
alter table agent drop foreign key fk_agent_ib_id, add constraint fk_agent_update_ib_id foreign key  (ib_id) references buero(id) on delete set null;
	
# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;

alter table agent add constraint fk_agent_ib_id foreign key (ib_id) references buero(id)  on delete cascade on update cascade;
alter table user modify column joining_date datetime not null;
alter table user modify column mobile varchar(255) not null;
alter table user modify column email varchar(255) not null;
alter table user drop column reset_password;  
alter table user add column role varchar(255);

SET FOREIGN_KEY_CHECKS = 1;
