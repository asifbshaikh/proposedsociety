# --- !Ups
create table page (
  id                        bigint auto_increment not null,
  formatted_title           varchar(255) not null,
  title                     varchar(255) not null,
  content                   text,
  constraint pk_page primary key (id)
);

create table spouse_details (
  id                        bigint auto_increment not null,
  sp_title                  varchar(4) not null,
  sp_fname                  varchar(50) not null,
  sp_mname                  varchar(50) not null,
  sp_lname                  varchar(50),
  sp_profession             varchar(50) not null,
  sp_dob                    datetime not null,
  anniversary               datetime not null,
  children                  integer,
  constraint pk_spouse_details primary key (id)
);

create table address (
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

create table employed_income (
  id                    bigint auto_increment not null,
  employer              varchar(100) not null,
  designation           varchar(50) not null,
  gross_sal             bigint not null,
  deductions            bigint,
  net_sal               bigint not null,
  yrly_bonus            bigint,
  yrs_with_employer     int not null,
  experience            bigint not null,
  yrs_service_remn      int not null,
  constraint pk_employed_income primary key (id)
);

create table self_employed_income (
  id                   		bigint auto_increment not null,
  business_type        		varchar(50) not null,
  yr1_pat              		bigint not null,
  yr1_depr             		bigint not null,
  yr2_pat              		bigint,
  yr2_depr             		bigint,
  yr3_pat              		bigint,
  yr3_depr             		bigint,
  constraint pk_self_employed_income primary key (id)
);

create table applicant (
  # Applicant name and details.
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
  fh_title                  varchar(4) not null,
  fh_fname                  varchar(50) not null,
  fh_mname                  varchar(50) not null,
  fh_lname                  varchar(50),
  fh_relation               varchar(50) not null,

  # Details of Spouse if married.
  marital_status            char not null,
  spouse_details_id         bigint, 
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

  approx_eligibility        bigint,
  
  # Income Details.
  occupation                varchar(50) not null,
  employed_income_id        bigint,
  self_employed_income_id   bigint,

  # Details of Loan if any. Note: Here loan_shown_on_bs or amount_per_month_from_sal should not be asked.   
  loan                      tinyint(1) default 0,
  constraint pk_applicant primary key (id)
);


create table form (
  id                        bigint auto_increment not null,
  applicant_id              bigint,
  co_applicant_id           bigint,
  form_number               varchar(50) not null,
  status                    varchar(50) not null,
  constraint pk_application_form primary key (id)
);

create table user (
  id                        bigint auto_increment not null,
  email                     varchar(100) not null,
  name                      varchar(100),
  mobile                    varchar(20) not null,
  password                  varchar(255),
  authcode                  varchar(10),
  role                      varchar(20) not null,
  joining_date              datetime not null,
  last_login                datetime,
  email_verified            tinyint(1) default 0,
  phone_verified            tinyint(1) default 0,
  form_id                   bigint,
  constraint pk_user primary key (id)
);

create table loan_details (
  id                        bigint auto_increment not null,
  applicant_id              bigint,
  financer                  varchar(100),
  loan_amount               bigint,
  monthly_emi               bigint,
  installments_paid         integer,
  installments_balance      integer,
  balance_loan_amount       bigint,
  constraint pk_loan_details primary key (id)
);

create table dependent (
  id                        bigint auto_increment not null,
  dep_title                 varchar(4) not null,
  dep_relation              varchar(50) not null,
  dep_fname                 varchar(50) not null,
  dep_mname                 varchar(50) not null,
  dep_lname                 varchar(50) not null,
  applicant_id              bigint,
  constraint pk_dependent primary key (id)
);

create table bank_account (
  id                        bigint auto_increment not null,
  own_contribution_id       bigint not null,
  bank_name                 varchar(100) not null,
  account_number            varchar(50),
  balance                   bigint not null,
  any_ded_from_bank_account tinyint(1) default 0,

  loan                      tinyint(1) default 0,
  loan_emi                  bigint,
  balance_installments      integer,
  balance_loan_amount       bigint,
  other_monthly_deduction   bigint,
  total_monthly_deduction   bigint,
  allocate_to_buy           bigint,
  form_id                   bigint not null,
  constraint pk_bank_account primary key (id)
);

create table fixed_deposit (
  id                        bigint auto_increment not null,
  bank_name                 varchar(100) not null,
  name_of_holder            varchar(100) not null,
  start_date                datetime not null,
  maturity_date             datetime not null,
  pricipal                  bigint not null,
  interest_rate             decimal(5, 2) not null,
  maturity_amount           bigint not null,
  have_taken_loan_on_fd     tinyint(1) default 0,
  monthly_amnt_dedcted_frm_sal tinyint(1) default 0,

  # Loan details
  financer                  varchar(100),
  loan_amount               bigint,
  montly_emi                bigint,
  installments_paid         integer,
  balance_installments      integer,
  balance_loan_amount       bigint,

  will_surrender            tinyint(1) default 0,
  allocation                bigint,
  amount_can_avail          bigint,
  form_id                   bigint not null,
  constraint pk_fixed_deposit primary key (id)
);

create table insurance (
  id                        bigint auto_increment not null,
  name_of_holder            varchar(100) not null,
  insurer_name              varchar(100) not null,
  start_date                datetime not null,
  maturity_date             datetime not null,
  sum_assured               bigint not null,
  premium_freqency          varchar(20),
  premium                   bigint not null,
  premiums_paid             integer,
  premiums_balance          integer,
  last_payment_date         datetime,
  maturity_amount           bigint not null,
  monthly_amnt_dedcted_frm_sal tinyint(1) default 0,
  receiving_any_matured_amount tinyint(1) default 0,
  allocation                bigint,
  form_id                   bigint,
  constraint pk_insurance primary key (id)
);

create table recurring_deposit (
  id                            bigint auto_increment not null,
  bank_name                     varchar(100) not null,
  name_of_holder                varchar(100) not null,
  start_date                    datetime,
  maturity_date                 datetime,
  installment                   bigint not null,
  installments_paid             integer,
  installments_balance          integer,
  principal                     bigint,
  interest_rate                 decimal(5, 2),
  maturity_amount               bigint not null,
  monthly_installments          bigint,
  balance_installments          integer,
  balance_ded_amount            bigint,
  monthly_amnt_dedcted_frm_sal  tinyint(1) default 0,
  will_surrender                tinyint(1) default 0,
  amount_can_avail              bigint,
  allocation                    bigint,
  form_id                       bigint not null,
  constraint pk_recurring_deposit primary key (id)
);

create table misc_borrowing (
  id                        bigint auto_increment not null,
  amount                    bigint not null,
  number_of_years           integer,
  type                      varchar(11) not null,
  interest_rate             decimal(5,2),
  emi                       bigint,
  total_deduction           bigint,
  form_id                   bigint not null,
  constraint pk_misc_borrowing primary key (id)
);

create table property (
  id                        bigint auto_increment not null,
  type                      varchar(50),
  type_detail               varchar(50),
  location_id               bigint,
  area                      decimal(38),
  loan                      tinyint(1) default 0,
  monthly_amnt_dedcted_frm_sal tinyint(1) default 0, 
  loan_details_id           bigint,
  willing_to_sell           tinyint(1) default 0,
  expected_price            bigint,
  allocation                bigint,

    # Loan details
  financer                  varchar(100),
  loan_amount               bigint,
  montly_emi                bigint,
  installments_paid         integer,
  installments_balance      integer,
  principal                 integer,
  interest_rate             decimal(38),
  maturity_amount           integer not null,
  monthly_installments      integer,
  balance_installments      integer,
  balance_loan_amount       bigint,

  form_id                   bigint not null,
  constraint pk_property primary key (id)
);


# TODO:
create table external_amenity (
  id                        bigint auto_increment not null,
  external_amenity_name       varchar(255),
  if_other_text               varchar(255),
  requirement_id                   bigint not null,
  constraint pk_external_amenity primary key (id))
;

create table internal_amenity (
  id                        bigint auto_increment not null,
  internal_amenity_name       varchar(255),
  if_other_text               varchar(255),
  requirement_id                   bigint not null,
  constraint pk_internal_amenity primary key (id))
;

create table nearby (
  id                        bigint auto_increment not null,
  location_near               varchar(255),
  if_other_text               varchar(255),
  requirement_id                   bigint not null,
  constraint pk_nearby primary key (id))
;

create table requirement (
	id                        			bigint auto_increment not null,
	builtup_area						integer,		
	carpet_area							integer,		
	choice_of_floor						integer,   
	sub_type                      		varchar(255),     
	garden_area							integer, 	
	garden_area_required                varchar(255),		
	no_of_bedrooms						integer,      
	no_of_common_toilets				integer,
	no_of_rooms_required				integer,      
	no_of_separate_bathroom				integer,      
	no_of_separate_wc					integer,      
	open_space							integer,		
	plot_area							integer,		
	plot_area_measure                   varchar(255),    
	terrace_area						integer,		
	terrace_required          			tinyint(1) default 0,
	terrace_choice						varchar(255),		
	toilet_wc_bath_requirements         varchar(255),		
	use_of_land                      	varchar(255),
	other_details                      	varchar(255),
	type                      			varchar(255),		
	residential_type_detail             varchar(255),  
	commercial_type_detail              varchar(255),
	agricultural_type_detail            varchar(255),
	type_detail							varchar(255),
	specify_height_required          	tinyint(1) default 0,
	height								integer,	
	power_connection_type               varchar(255),
	if_connection_type_other            varchar(255),
	is_separate_water_connection_required          			tinyint(1) default 0,
	is_separate_power_connection_required          			tinyint(1) default 0,
	loft_required          				tinyint(1) default 0,
	loft_area							integer,		
	otla_required          				tinyint(1) default 0,
	otla_area							integer,
	no_of_attached_toilets				integer,
	no_of_attached_urinals				integer,		
	open_space_required          		tinyint(1) default 0,
	toilet_type                      	varchar(255),
	water_pipe_size						integer,
	if_chawl_type_other                 varchar(255),
	area_for_each_room					integer,
	city                      			varchar(255),
	taluka                      		varchar(255),
	district                      		varchar(255),
	state                      			varchar(255),
	country                      		varchar(255),
	village_or_detail_location          varchar(255),
	constraint pk_requirement primary key (id))
;

###### Custom constraint #######

# Page table constraints
alter table page add constraint uk_page__fomatted_title unique index(formatted_title);

# Address table constraints
create index ix_address__state on address(state); 
create index ix_address__city on address(city);
create index ix_address__area on address(area);
create index ix_address__pin on address(pin);

# LoanDetails table constraints
alter table loan_details add constraint fk_loan_details__applicant_id foreign key(applicant_id) references applicant(id) on delete cascade on update cascade;

# Applicant table constraints

alter table applicant add constraint fk_applicant__spouse_details_id foreign key (spouse_details_id) references spouse_details(id) on delete restrict on update restrict;
alter table applicant add constraint fk_applicant__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;
alter table applicant add constraint fk_applicant__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;
alter table applicant add constraint fk_applicant__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;
alter table applicant add constraint fk_applicant__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;
alter table applicant add constraint fk_applicant__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;
alter table applicant add constraint fk_applicant__employed_income_id foreign key (employed_income_id) references employed_income(id) on delete set null;
alter table applicant add constraint fk_applicant__self_employed_income_id foreign key (self_employed_income_id) references self_employed_income(id) on delete set null;

# Form table constraints
alter table form add constraint uk_form__form_number unique index(form_number);
alter table form add constraint fk_form__applicant_id foreign key (applicant_id) references applicant(id) on delete restrict on update restrict;
alter table form add constraint fk_form__co_applicant_id foreign key (co_applicant_id) references applicant(id) on delete set null;

# User table constraints
alter table user add constraint uk_user_email unique index (email);
alter table user add constraint uk_user_mobile unique index (mobile);
alter table user add constraint fk_user__form_id foreign key(form_id) references form(id) on delete set null;

# Dependent table constraints
alter table dependent add constraint fk_dependent__applicant_id foreign key(applicant_id) references applicant(id) on delete cascade on update cascade;

# bank_account table constraints
alter table bank_account add constraint fk_bank_account__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;

# fixed_deposit table constraints
alter table fixed_deposit add constraint fk_fixed_deposit__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;

# insurance table constraints
alter table insurance add constraint fk_insurance__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;

# recurring_deposit table constraints
alter table recurring_deposit add constraint fk_recurring_deposit__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;

# misc_borrowing table constraints
alter table misc_borrowing add constraint fk_misc_borrowing__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;

# property table constraints
alter table property add constraint fk_property__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;

# nearby table constraints
alter table nearby add constraint uk_nearby unique (requirement_id,location_near);
alter table nearby add constraint fk_nearby__requirement_id foreign key (requirement_id) references requirement(id) on delete cascade on update cascade;

# external_amenity table constraints
alter table external_amenity add constraint uk_external_amenity unique (requirement_id,external_amenity_name);
alter table external_amenity add constraint fk_external_amenity__requirement_id foreign key (requirement_id) references requirement(id) on delete cascade on update cascade;

# internal_amenity table constraints
alter table internal_amenity add constraint uk_internal_amenity unique (requirement_id,internal_amenity_name);
alter table internal_amenity add constraint fk_internal_amenity__requirement_id foreign key (requirement_id) references requirement(id) on delete cascade on update cascade;








# --- !Downs

SET FOREIGN_KEY_CHECKS=0;
drop table page;
drop table spouse_details;
drop table address;
drop table loan_details;
drop table employed_income;
drop table self_employed_income;
drop table applicant;
drop table form;
drop table user;
drop table dependent;
drop table bank_account;
drop table fixed_deposit;
drop table insurance;
drop table recurring_deposit;
drop table misc_borrowing;
drop table property;
drop table requirement;
drop table external_amenity;
drop table internal_amenity;
drop table nearby;
SET FOREIGN_KEY_CHECKS=1;
