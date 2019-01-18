
# --- !Ups

alter table agent_form modify column is_income_tax_payee varchar(6) not null ;
alter table agent_form modify column online_monatory_transaction_facility varchar(6) not null;
update agent_form set is_income_tax_payee = 'true ' where is_income_tax_payee = '1' ;
update agent_form set is_income_tax_payee = 'false ' where is_income_tax_payee = '0' ;
update agent_form set online_monatory_transaction_facility = 'true' where online_monatory_transaction_facility = '1' ;
update agent_form set online_monatory_transaction_facility = 'false' where online_monatory_transaction_facility = '0' ;

#Desired location to buy property address format change
alter table requirement_address change city pincode varchar(255);
alter table requirement_address drop column country;
alter table requirement_address  change village_or_detail_location locality varchar(100);

create table agent_commission (

	id 					bigint auto_increment not null ,
	commission_amount 	decimal(15,2),
	description      	varchar(255),
	commission_date	 	date,
	commission_against_id  		bigint,
	agent_id					bigint not null,
	constraint pk_agent_commission primary key (id)
	
);

alter table payment add column payment_mode varchar(255) ;

#Handling agent filling application form behalf of applicant

alter table user add column agent_id bigint ;
alter table user add constraint fk_user_agent_id foreign key (agent_id) references agent(id)  on delete set null;
alter table agent_commission add constraint fk_agent_commission_agent_id foreign key (agent_id) references agent(id)  on delete cascade on update cascade;

# handling statff user 
alter table staff_user add constraint staff_user_email_unique_key unique(email);
alter table staff_user add column agent_id bigint ;
alter table staff_user add constraint fk_staff_user_agent_id  foreign key (agent_id) references agent(id)  on delete cascade on update cascade;
alter table staff_user modify role varchar(300);

# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;
alter table staff_user modify role varchar(50);
alter table staff_user drop foreign key fk_staff_user_agent_id ;
alter table staff_user drop column agent_id ;
alter table staff_user  drop index staff_user_email_unique_key ;
alter table payment drop column payment_mode ;
alter table requirement_address  change locality village_or_detail_location varchar(100);
alter table requirement_address add column country varchar(100);
alter table requirement_address change pincode city varchar(255);
alter table user drop foreign key fk_user_agent_id ;
alter table user drop column agent_id ;
drop table agent_commission ;

SET FOREIGN_KEY_CHECKS = 1;
