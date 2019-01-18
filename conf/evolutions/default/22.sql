# --- !Ups

#Agent Form 
alter table agent drop column resi_phone;
alter table agent drop column type_resi_phone;
alter table agent add column nri_address_id bigint;
alter table agent add column office_address_same_as varchar(20) not null;
alter table agent add constraint fk_agent_nri_address_id foreign key (nri_address_id) references address(id)  on delete set null;
alter table agent modify column  office_address_id bigint;
alter table agent modify column  off_mail  			varchar(100);
alter table agent modify column website_addres 	 	varchar(100) ;
alter table agent modify column  off_fax       		varchar(100) ;
alter table agent modify column  off_phone1      	varchar(20) ;
alter table agent modify column off_phone1_type  	varchar(20);
alter table agent modify column off_phone2        	varchar(20);
alter table agent modify column  off_phone2_type    varchar(20) ;
alter table agent modify column off_phone3         varchar(20) ;
alter table agent modify column off_phone3_type  	varchar(20) ;
alter table agent modify column identity2_id  	bigint ;
alter table agent modify column identity3_id  	bigint ;

#Attachment Ids
alter table agent add column resi_light_bill_id  bigint;
alter table agent add column resi_tax_receipt_id  bigint;
alter table agent add column office_light_bill_id  bigint;
alter table agent modify column office_addressproof_id bigint;

alter table agent add constraint fk_agent_resi_light_bill_id	foreign key (resi_light_bill_id) references attachment(id) on delete restrict on update restrict;
alter table agent add constraint fk_agent_resi_tax_receipt_id	foreign key (resi_tax_receipt_id) references attachment(id) on delete restrict on update restrict;
alter table agent add constraint fk_agent_office_light_bill_id	foreign key (office_light_bill_id) references attachment(id) on delete restrict on update restrict;

# Issue #101 Adding multiple roles for the user.
create table role (
  id                      bigint auto_increment not null,
  user_id                 bigint not null,
  role                    varchar(50) not null,
  constraint pk_role primary key(id)
);

alter table role add constraint fk_role__user_id foreign key (user_id) references user(id) on delete cascade on update cascade;
alter table role add constraint uk_role__user_id__role unique (user_id, role);
insert into role(user_id, role) select id, role from user;


# Issue #101 removing not null constraint on the role column. It maybe safe to delete, but lets keep it there for now. 
alter table user modify column role varchar(20);

# Issue 99 Payment schema changes for CCAvenue payment gateway.

alter table payment add column auth_desc varchar(10);
alter table payment add column transaction_id varchar(50);

alter table payment drop column response_code;
alter table payment drop column response_message;
alter table payment drop column merchant_txn_id;
alter table payment drop column epg_txn_id;
alter table payment drop column auth_code;
alter table payment drop column prn;
alter table payment drop column cvresp_code;

select id from user;




# --- !Downs


#agent Form

SET FOREIGN_KEY_CHECKS=0;

alter table agent add column resi_phone    varchar(20) not null;
alter table agent add column type_resi_phone  varchar(20) not null;
alter table agent drop foreign key fk_agent_nri_address_id;
alter table agent drop column nri_address_id;
alter table agent drop column office_address_same_as;
alter table agent modify column office_address_id bigint;
alter table agent modify column off_mail varchar(100) not null;
alter table agent modify column website_addres 	 	varchar(100) not null;
alter table agent modify column off_fax varchar(100) not null;
alter table agent modify column off_phone1      	varchar(20) not null;
alter table agent modify column off_phone1_type  	varchar(20) not null;
alter table agent modify column off_phone2        	varchar(20) not null;
alter table agent modify column off_phone2_type    varchar(20) not null;
alter table agent modify column off_phone3         varchar(20) not null;
alter table agent modify column off_phone3_type  	varchar(20) not null;
alter table agent modify column identity2_id  	bigint not null ;
alter table agent modify column identity3_id  	bigint not null;

alter table agent drop foreign key fk_agent_resi_light_bill_id ;
alter table agent drop foreign key fk_agent_resi_tax_receipt_id;
alter table agent drop foreign key fk_agent_office_light_bill_id;
alter table agent drop column resi_light_bill_id;
alter table agent drop column resi_tax_receipt_id;
alter table agent drop column office_light_bill_id;
alter table agent modify column office_addressproof_id bigint not null;

drop table if exists role;
alter table user modify column role varchar(20) not null;

SET FOREIGN_KEY_CHECKS=1;

# Issue 99
alter table payment add column response_code char;
alter table payment add column response_message varchar(255);
alter table payment add column merchant_txn_id varchar(50);
alter table payment add column epg_txn_id varchar(15);
alter table payment add column auth_code varchar(6);
alter table payment add column prn varchar(12);
alter table payment add column cvresp_code varchar(1);

alter table payment drop column auth_desc;
alter table payment drop column transaction_id;

