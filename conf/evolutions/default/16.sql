# --- !Ups
alter table loan_details drop foreign key fk_loan_details__applicant_id;
alter table dependent drop foreign key fk_dependent__applicant_id;

ALTER TABLE loan_details CHANGE applicant_id form_id bigint;
ALTER TABLE dependent CHANGE applicant_id form_id bigint;

alter table loan_details add constraint fk_loan_details__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;
alter table dependent add constraint fk_dependent__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;

alter table dependent add column is_applicant tinyint(1) default 0;
alter table loan_details add column is_applicant tinyint(1) default 0;

# Adding column for storing authentication code hash.
alter table user add column authcode_hash varchar(255);

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
  identity_number1          varchar(50) not null,
  identity_number1_type     varchar(20) not null,
  identity_number2          varchar(50),
  identity_number2_type     varchar(20),
  identity_number3          varchar(50),
  identity_number3_type     varchar(20),
  
  marital_status            char not null,
  nationality               varchar(50) not null,

  permanent_address_same_as varchar(20) not null,
  residential_address_same_as varchar(20) not null,
  communication_address_same_as varchar(20) not null,

  birth_place_id            bigint not null,
  permanent_address_id      bigint,
  residential_address_id    bigint,
  communication_address_id  bigint,
  resi_status               varchar(50) not null,
  residence_yrs_residing    integer not null,
  city_yrs_residing         integer not null,
  phone1_type               varchar(20) not null,
  phone1                    varchar(20) not null,
  phone2_type               varchar(20),
  phone2                    varchar(20),
  phone3_type               varchar(20),
  phone3                    varchar(20),

  office_address_id         bigint not null,
  off_mail                  varchar(100),
  employer_web              varchar(100),
  off_phone1                varchar(20) not null,
  off_phone1_type           varchar(20) not null,
  off_phone2                varchar(20),
  off_phone2_type           varchar(20),
  off_phone3                varchar(20),
  off_phone3_type           varchar(20),

  
  #Agent specific 
  occupation                varchar(200) not null,
  qualification             varchar(200) not null,
  agent_type                varchar(1) not null,
  company_name              varchar(200) not null,
  is_income_tax_payee       char not null,
  pan_card_number           varchar(10) not null,
  online_monatory_transaction_facility char not null,

	#Agent Bank Details
  bank_name				varchar(300) not null,
	branch_name				varchar(300) not null,
	card_type				char not null,
	card_number				varchar(20),
	card_expiry_date		datetime not null,
	amount_authorised_currency varchar(4) not null,
	amount_authorised		bigint not null,
	
	any_criminal_record		char not null,
	
  constraint pk_agent primary key (id)
);

create table language_proficiency(

    id                      bigint auto_increment not null,
    name                    varchar(100) not null,
    can_read                    char not null,
    can_write                   char not null,
    can_speak                   char not null,
    agent_id                bigint not null,
    constraint pk_language_proficiency primary key(id)
);

# Agent table constraints

alter table agent add constraint fk_agent__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;
alter table agent add constraint fk_agent__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;
alter table agent add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;
alter table agent add constraint fk_agent__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;
alter table agent add constraint fk_agent__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;

# language_proficiency table constraints
alter table language_proficiency add constraint fk_agent_id foreign key (agent_id)  references  agent(id) on delete restrict on update restrict;

# Adding column for experience_month,experience_year,yrs_with_employer
alter table employed_income add column experience_year int ;
alter table employed_income add column experience_month int;
alter table employed_income add column yrs_with_employer_d datetime;
alter table employed_income drop column experience;
alter table employed_income drop column yrs_with_employer;


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

alter table loan_details drop column is_applicant;
alter table dependent drop column is_applicant;

alter table loan_details drop foreign key fk_loan_details__form_id;
alter table dependent drop foreign key fk_dependent__form_id;

ALTER TABLE loan_details CHANGE form_id applicant_id bigint;
ALTER TABLE dependent CHANGE form_id applicant_id bigint;

alter table loan_details add constraint fk_loan_details__applicant_id foreign key(applicant_id) references applicant(id) on delete cascade on update cascade;
alter table dependent add constraint fk_dependent__applicant_id foreign key(applicant_id) references applicant(id) on delete cascade on update cascade;

alter table user drop column authcode_hash;

drop table if exists agent;
drop table if exists language_proficiency;


alter table employed_income drop column experience_year;
alter table employed_income drop column experience_month;
alter table employed_income drop column yrs_with_employer_d;
alter table employed_income add column yrs_with_employer int not null;
alter table employed_income add column experience bigint not null;

SET FOREIGN_KEY_CHECKS=1;
