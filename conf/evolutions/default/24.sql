# --- !Ups
SET FOREIGN_KEY_CHECKS=0;

delete from address where id in (
									select office_address_id from agent_form  where office_address_id is not null  
									union
									select communication_address_id from agent_form  where communication_address_id is not null 
									union
									select birth_place_id from agent_form  where birth_place_id is not null 
									union
									select residential_address_id from agent_form  where residential_address_id is not null 
									union
									select  permanent_address_id from agent_form  where  permanent_address_id is not null
									
								);
truncate table agent ;
truncate table agent_form ;

SET FOREIGN_KEY_CHECKS=1;

#Issue116
alter table applicant modify column  identity_number1 varchar(50);
alter table applicant modify column identity_number1_type varchar(20) ;
alter table applicant modify column permanent_address_same_as varchar(20);
alter table applicant modify column  residential_address_same_as varchar(20);
alter table applicant modify column  communication_address_same_as varchar(20);
alter table applicant modify column  resi_status varchar(50);
alter table applicant modify column  residence_yrs_residing integer;
alter table applicant modify column city_yrs_residing integer;
alter table applicant modify column  off_phone1 varchar(30);
alter table applicant modify column  off_phone1_type varchar(30);
alter table applicant modify column  occupation varchar(50);

alter table self_employed_income modify column business_type varchar(50);
alter table self_employed_income modify column yr1_pat varchar(20);
alter table self_employed_income modify column yr1_depr varchar(20);

alter table employed_income modify column  employer varchar(100) ;
alter table employed_income modify column designation varchar(50);
alter table employed_income modify column gross_sal bigint(20);
alter table employed_income modify column  net_sal bigint(20);
alter table employed_income modify column  yrs_service_remn int;

alter table spouse_details modify column sp_title varchar(4);
alter table spouse_details modify column sp_fname varchar(50);
alter table spouse_details modify column sp_mname varchar(50);
alter table spouse_details modify column sp_profession varchar(50);
alter table spouse_details modify column  sp_dob datetime ;
alter table spouse_details modify column anniversary datetime ;

#Adress to AgentAddress



create table agent_address (
  id                        bigint auto_increment not null,
  line1                     varchar(100) not null,
  line2                     varchar(100),
  street                    varchar(100),
  area                      varchar(100) not null, 
  taluka                    varchar(100),
  city                      varchar(100) not null,
  district                  varchar(100),
  state                     varchar(100) not null,
  country                   varchar(100) not null,
  pin                       varchar(50) not null,
  constraint pk_address primary key (id)
);

alter table agent_form drop foreign key   fk_agent__birth_place_id ;
alter table agent_form drop foreign key   fk_agent__residential_address_id ;
alter table agent_form drop foreign key   fk_agent__permanent_address_id ;
alter table agent_form drop foreign key   fk_agent__communication_address_id ;
alter table agent_form drop foreign key   fk_agent__office_address_id ;

alter table agent_form add constraint fk_agent__birth_place_id foreign key (birth_place_id) references agent_address(id) on delete restrict on update restrict;
alter table agent_form add constraint fk_agent__residential_address_id foreign key (residential_address_id) references agent_address(id) on delete set null;
alter table agent_form add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references agent_address(id) on delete set null;
alter table agent_form add constraint fk_agent__communication_address_id foreign key (communication_address_id) references agent_address(id) on delete set null;
alter table agent_form add constraint fk_agent__office_address_id foreign key (office_address_id) references agent_address(id) on delete restrict on update restrict;

update loan_details set amount_per_month_from_sal = 1 where loan_shown_on_bs = 1;

# Settings form's status as "Paid" for forms of users who have already paid the form fee.
 update form set status = 'Paid' where id in (select form_id from user where id in (select distinct user_id from invoice where paid = 1));

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

alter table applicant modify column  identity_number1 varchar(50) not null  ; 
alter table applicant modify column identity_number1_type varchar(20)  not null ; 
alter table applicant modify column permanent_address_same_as varchar(20) not null ; 
alter table applicant modify column  residential_address_same_as varchar(20) not null ; 
alter table applicant modify column  communication_address_same_as varchar(20) not null ; 
alter table applicant modify column  resi_status varchar(50) not null ; 
alter table applicant modify column  residence_yrs_residing integer not null ; 
alter table applicant modify column city_yrs_residing integer not null ; 
alter table applicant modify column  off_phone1 varchar(30) not null ; 
alter table applicant modify column  off_phone1_type varchar(30) not null ; 
alter table applicant modify column  occupation varchar(50) not null ; 

alter table self_employed_income modify column business_type varchar(50) not null ;
alter table self_employed_income modify column yr1_pat varchar(20) not null ;
alter table self_employed_income modify column yr1_depr varchar(20) not null ;

alter table employed_income modify column  employer varchar(100)  not null ;
alter table employed_income modify column designation varchar(50) not null ;
alter table employed_income modify column gross_sal bigint(20) not null ;
alter table employed_income modify column  net_sal bigint(20) not null ;
alter table employed_income modify column  yrs_service_remn int not null ;


alter table spouse_details modify column sp_title varchar(4) not null ;
alter table spouse_details modify column sp_fname varchar(50) not null ;
alter table spouse_details modify column sp_mname varchar(50) not null ;
alter table spouse_details modify column sp_profession varchar(50) not null ;
alter table spouse_details modify column  sp_dob datetime  not null ;
alter table spouse_details modify column anniversary datetime  not null ;

# Agent table constraints

alter table agent_form drop foreign key   fk_agent__birth_place_id ;
alter table agent_form drop foreign key   fk_agent__residential_address_id ;
alter table agent_form drop foreign key   fk_agent__permanent_address_id ;
alter table agent_form drop foreign key   fk_agent__communication_address_id ;
alter table agent_form drop foreign key   fk_agent__office_address_id ;

alter table agent_form add constraint fk_agent__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;
alter table agent_form add constraint fk_agent__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;
alter table agent_form add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;
alter table agent_form add constraint fk_agent__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;
alter table agent_form add constraint fk_agent__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;

drop table agent_address;
update loan_details set amount_per_month_from_sal = 0 where  loan_shown_on_bs = 1;

SET FOREIGN_KEY_CHECKS=1;
