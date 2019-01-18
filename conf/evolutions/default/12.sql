# --- !Ups
alter table summary add column all_bank_bal_summ_amnt tinyint(1) default 0;

# --- !Downs
alter table summary drop column all_bank_bal_summ_amnt;