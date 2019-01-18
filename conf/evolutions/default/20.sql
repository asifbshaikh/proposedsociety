# --- !Ups
SET FOREIGN_KEY_CHECKS=0;
drop table if exists agent;
drop table if exists language_proficiency;
SET FOREIGN_KEY_CHECKS=1;

create table language_proficiency(

    id                      bigint auto_increment not null,
    name                    varchar(100) not null,
    can_read                    tinyint(1)  ,
    can_write                  tinyint(1)  ,
    can_speak                  tinyint(1)  ,
    constraint pk_language_proficiency primary key(id)
);

create table agent(
# Agent name and details.
  id                        bigint auto_increment not null,
  title                     varchar(4) not null,
  fname                     varchar(50) not null,
  mname                     varchar(50) not null,
  lname                     varchar(50),
  dob                       datetime not null,
  email                     varchar(100) not null,
  sex                       char not null,
  fh_title                  varchar(4) not null,
  fh_fname                  varchar(50) not null,
  fh_mname                  varchar(50) not null,
  fh_lname                  varchar(50),
  fh_relation               varchar(50) not null,

  identity_number1          varchar(50) ,
  identity_number1_type     varchar(20) ,
  identity_number2          varchar(50),
  identity_number2_type     varchar(20),
  identity_number3          varchar(50),
  identity_number3_type     varchar(20),

  phone1_type               varchar(20) not null,
  phone1                    varchar(20) not null,
  phone2_type               varchar(20),
  phone2                    varchar(20),
  phone3_type               varchar(20),
  phone3                    varchar(20),
  
  marital_status            char not null,
  nationality               varchar(50) not null,


#Agent Address Details
  permanent_address_same_as varchar(20) not null,
  residential_address_same_as varchar(20) not null,
  communication_address_same_as varchar(20) not null,

  birth_place_id            bigint not null,
  permanent_address_id      bigint,
  residential_address_id    bigint,
  communication_address_id  bigint,

  #Agent - Residential Status
  resi_status               varchar(50) not null,
  residence_yrs_residing    integer not null,
  city_yrs_residing         integer not null,
  resi_phone                varchar(20) not null,
  type_resi_phone           varchar(20) not null,
  

#Agent Office Address Details
  office_address_id         bigint not null,
  off_mail                  varchar(100) not null,
  website_addres            varchar(100) not null,
  off_fax                   varchar(100) not null,
  off_phone1                varchar(20) not null,
  off_phone1_type           varchar(20) not null,
  off_phone2                varchar(20) not null,
  off_phone2_type           varchar(20) not null,
  off_phone3                varchar(20) not null,
  off_phone3_type           varchar(20) not null,
    
  #Agent Language Proficiency(fk)
  hindi_id  bigint not null,
  marathi_id  bigint not null,
  english_id   bigint not null,

	#Agent Bank Details
  bank_name				           varchar(300) not null,
	branch_name			           varchar(300) not null,
	bank_account               bigint not null,

  #Status of Agent
  agent_type                varchar(100) not null,
  company_name              varchar(200) not null,                  
  occupation                varchar(200) not null,
  qualification             varchar(200) not null,
  other_qualification       varchar(100) ,
  is_income_tax_payee       tinyint(1) not null,
  pan_card_number           varchar(10) not null,
  online_monatory_transaction_facility  tinyint(1) not null,

  #Form Place And Day
  form_filled_place         varchar(200) ,
  form_filled_day           varchar(200) ,
	
  constraint pk_agent primary key (id)
);

# Agent table constraints

alter table agent add constraint fk_agent__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;
alter table agent add constraint fk_agent__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;
alter table agent add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;
alter table agent add constraint fk_agent__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;
alter table agent add constraint fk_agent__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;
alter table agent add constraint fk_hindi_language_proficiency_id foreign key (hindi_id) references language_proficiency(id) on delete restrict on update restrict;
alter table agent add constraint fk_marathi_language_proficiency_id foreign key (marathi_id) references language_proficiency(id) on delete restrict on update restrict;
alter table agent add constraint fk_english_language_proficiency_id foreign key (english_id) references language_proficiency(id) on delete restrict on update restrict;

# Increasing size of last_login_ip. We are potentially retrieving IPv6 address which is longer.
alter table user modify last_login_ip varchar(100);

# --- !Downs
SET FOREIGN_KEY_CHECKS=0;
drop table if exists agent;
drop table if exists language_proficiency;
SET FOREIGN_KEY_CHECKS=1;