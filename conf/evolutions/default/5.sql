# --- !Ups
CREATE TABLE property_address (
  id            bigint(20) NOT NULL AUTO_INCREMENT,
  line1         varchar(100) NOT NULL,
  city 				  varchar(100) NOT NULL,
  taluka 			  varchar(100) DEFAULT NULL,
  district   	  varchar(100) DEFAULT NULL,
  state 			  varchar(100) NOT NULL,
  country 		  varchar(100) NOT NULL,
  pin 				  varchar(50) NOT NULL,
  constraint pk_property_address primary key (id)
);

create table invoice (
  id                   bigint(20) NOT NULL AUTO_INCREMENT,
  invoice_number       varchar(50) NOT NULL,
  description          varchar(255),
  amount               decimal(15, 2) NOT NULL,
  paid                 tinyint(1) default 0,
  user_id              bigint(20),
  constraint pk_invoice primary key(id)
);

create table payment (
  id                   bigint(20) NOT NULL AUTO_INCREMENT,
  invoice_id			       bigint(20),
  start_time		       datetime NOT NULL,
  completion_time      datetime,
  response_code		     char,
  response_message	   varchar(255),
  merchant_txn_id      varchar(50),
  epg_txn_id		       varchar(15),
  auth_code			       varchar(6),
  prn                  varchar(12),
  cvresp_code		       varchar(1), 
  processed            tinyint(1) default 0,
  amount               decimal(15, 2) NOT NULL,
  redirect_url         varchar(1024),
  constraint pk_payment primary key(id)
);

alter table property add constraint fk_property__location_id foreign key (location_id) references property_address(id) on delete set null;

alter table invoice add constraint fk_invoice__user_id foreign key(user_id) references user(id) on delete set null on update set null;
alter table invoice add constraint uk_invoice__invoice_number unique index(invoice_number);

alter table payment add constraint fk_payment__invoice_id foreign key(invoice_id) references invoice(id) on delete set null on update set null;
alter table payment add constraint uk_payment__merchant_txn_id unique index(merchant_txn_id);
alter table payment add constraint uk_payment__epg_txn_id unique index(epg_txn_id);
create index ix_payment__response_code on payment(response_code);

# --- !Downs

alter table property drop foreign key fk_property__location_id;

drop table property_address;
drop table invoice;
drop table payment;
