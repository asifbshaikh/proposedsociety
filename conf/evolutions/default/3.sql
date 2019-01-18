# --- !Ups
alter table applicant modify column birth_place_id bigint;
alter table applicant modify column office_address_id bigint;

alter table applicant drop foreign key fk_applicant__office_address_id;
alter table applicant add constraint fk_applicant__office_address_id foreign key (office_address_id) references address(id) on delete set null;

alter table applicant drop foreign key fk_applicant__birth_place_id;
alter table applicant add constraint fk_applicant__birth_place_id foreign key (birth_place_id) references address(id) on delete set null;


# --- !Downs
alter table applicant modify column birth_place_id bigint not null;
alter table applicant modify column office_address_id bigint not null;

alter table applicant drop foreign key fk_applicant__office_address_id;
alter table applicant add constraint fk_applicant__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;

alter table applicant drop foreign key fk_applicant__birth_place_id;
alter table applicant add constraint fk_applicant__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;