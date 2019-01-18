# --- !Ups

alter table agent  add column ib_id bigint;

create table investigation_buero(
	id 				bigint auto_increment not null,	
	ib_name 		varchar(255),
	constraint pk_investigation_buero primary key(id)
); 
alter table agent add constraint fk_agent_ib_id foreign key (ib_id) references investigation_buero(id)  on delete cascade on update cascade;


alter table investigation_buero drop column ib_name;
alter table investigation_buero add column user_id bigint;
alter table investigation_buero add constraint fk_investigation_buero_user_id foreign key (user_id) references user(id)  on delete cascade on update cascade;
create table ib_feedback(
	id 				bigint auto_increment not null,	
	ib_id			bigint not null,
	agent_id		bigint not null,
	attachment_id   bigint not null,
	constraint pk_ib_feedback primary key(id)
);

alter table ib_feedback add constraint fk_ib_feedback_attachment_id foreign key (attachment_id) references attachment(id) on delete cascade on update cascade;
alter table ib_feedback add constraint fk_ib_feedback_ib_id foreign key (ib_id) references investigation_buero(id) on delete cascade on update cascade;
alter table ib_feedback add constraint fk_ib_feedback_agent_id foreign key (agent_id) references agent(id) on delete cascade on update cascade;
alter table attachment add constraint uk_attachment_file_path unique (file_path);
alter table requirement_address add street varchar(100);
rename TABLE  `investigation_buero` TO  `buero`;
alter table agent add column agent_code varchar(250);
alter table agent add constraint uk_agent_agent_code unique key (agent_code) ;
drop table if exists staff_user;

#Adding Applicant Password to user
alter table user add column applicant_password varchar(255); 

create table short_requirement_address (
	id                        			bigint auto_increment not null,
	short_form_id                bigint not null,
	city                      			varchar(255),
	taluka                      		varchar(255),
	district                      		varchar(255),
	state                      			varchar(255),
	country                      		varchar(255),
	pincode								varchar(6),
	village_or_detail_location          varchar(255),
	constraint pk_short_requirement_address primary key (id)
);

create table short_form(
	  id                        bigint auto_increment not null,
	  title                     varchar(4) not null,
	  fname                     varchar(50) not null,
	  mname                     varchar(50) not null,
	  lname                     varchar(50),
	  dob                       datetime not null,
	  email                     varchar(100) not null,
	  mobile_number				varchar(20) not null,
	  landline_number			varchar(20) not null,
	  applicant_income_per		varchar(50) not null,
	  applicant_income			decimal(15, 2) not null,
	  co_applicant_income_per	varchar(50) not null,
	  co_applicant_income		decimal(15, 2),
	  loan_amount_required		decimal(15, 2) not null,
	  pesonal_contribution_amount decimal(15, 2) not null,
	  total_budget				decimal(15, 2) not null,
	  registred_by				varchar(50) not null,
	  agent_identity 			varchar(100),
	  requirement_type			varchar(200),
	  carpet_area				int ,
	  built_up_area				int,
	  invoice_id				bigint,
	  constraint pk_short_form primary key (id)
);

# short_requirement_address table constraints
alter table short_requirement_address add constraint fk_short_requirement_address__short_form_id foreign key (short_form_id) references short_form(id) on delete cascade on update cascade;

# short_form table constraint 
alter table short_form add constraint fk_short_form_invoice_id foreign key (invoice_id) references invoice(id) on delete cascade on update cascade;

# Handling applicants , applicant_password for those have password
update user set applicant_password = password where password is not null;

# an additional text box for capturing the "sub-locality"
alter table short_requirement_address add column street varchar(100);



# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;



alter table short_requirement_address drop column street ;
alter table user drop column applicant_password ;
drop table short_form;
drop table short_requirement_address ;
rename TABLE  `buero` TO  `investigation_buero`;
drop table if exists ib_feedback;
alter table investigation_buero drop foreign key fk_investigation_buero_user_id;
alter table investigation_buero drop column user_id; 
alter table investigation_buero add column ib_name 	varchar(255);

alter table agent drop index uk_agent_agent_code ;
alter table agent drop column agent_code ;
drop index uk_attachment_file_path  on attachment;
alter table requirement_address drop column street;

alter table agent drop foreign key fk_agent_ib_id ;
drop table if exists investigation_buero;
alter table agent  drop column ib_id ;

SET FOREIGN_KEY_CHECKS = 1;