# --- !Ups
alter table bank_account modify column own_contribution_id bigint;

#removing non-used columns from_property_table
alter table property drop column principal;
alter table property drop column interest_rate;
alter table property drop column maturity_amount;
alter table property drop column monthly_installments;
alter table property drop column installments_balance;
alter table property drop column loan_details_id;

# --- !Downs
alter table bank_account modify column own_contribution_id bigint not null;

#readding non-used columns to_property_table
alter table property add column principal integer;
alter table property add column interest_rate decimal(38);
alter table property add column maturity_amount integer not null;
alter table property add column monthly_installments integer;
alter table property add column installments_balance integer;
alter table property add column loan_details_id bigint;
