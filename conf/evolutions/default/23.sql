# --- !Ups


alter table agent  add column status varchar(50);
rename table agent to agent_form;

# Issue #110  refactoring agent
create table agent(
        id              bigint auto_increment not null,
        user_id         bigint not null ,
        form_id         bigint ,
        total_amount_paid bigint ,
        form_credits  bigint ,
        total_form_filled_count  bigint ,
        primary key(id)
);
alter table agent add constraint fk_agent__user_id foreign key (user_id) references user(id) on delete cascade on update cascade;
alter table agent add constraint fk_agent_form_id foreign key (form_id)	 references agent_form(id) on delete cascade on update cascade;

alter table requirement add column  requirement_sub_type varchar(255);

create table requirement_address (
	id                        			bigint auto_increment not null,
	requirement_id                      bigint not null,
	city                      			varchar(255),
	taluka                      		varchar(255),
	district                      		varchar(255),
	state                      			varchar(255),
	country                      		varchar(255),
	village_or_detail_location          varchar(255),
	constraint pk_requirement_address primary key (id)
);

# requirement_address table constraints
alter table requirement_address add constraint fk_requirement_address__requirement_id foreign key (requirement_id) references requirement(id) on delete cascade on update cascade;

# --- !Downs
SET FOREIGN_KEY_CHECKS=0;
drop table agent;
rename table agent_form to agent;
alter table agent drop column status;

alter table requirement drop column  requirement_sub_type;
drop table requirement_address;
SET FOREIGN_KEY_CHECKS=0;


# Note ---- Have to drop column user_id from agent_form  