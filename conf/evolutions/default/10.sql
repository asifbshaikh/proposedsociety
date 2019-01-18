# --- !Ups
create table summary (
  id                        bigint auto_increment not null,
  i_accpt_abv_cal                  tinyint(1) default 0,
  approx_loan_eli                 tinyint(1) default 0,
  cash_balance                   tinyint(1) default 0,
  all_fd_summ_amnt                 tinyint(1) default 0,
  all_rd_summ_amnt                 tinyint(1) default 0,
  all_insurance_summ_amnt          tinyint(1) default 0,
  selling_summ_amnt               tinyint(1) default 0,
  borrowed_summary               tinyint(1) default 0,
  donation_summary               tinyint(1) default 0,
  returned_amount                tinyint(1) default 0,
  constraint pk_summary primary key (id)
);

alter table form add column summary_id bigint;
alter table form add constraint fk_form__summary_id foreign key (summary_id) references summary(id) on delete set null;

# --- !Downs
alter table form drop foreign key fk_form__summary_id;
alter table form drop column summary_id bigint;

drop table summary;