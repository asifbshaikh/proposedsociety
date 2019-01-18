# --- !Ups

alter table loan_details add column amount_per_month_from_sal tinyint(1) default 0;
alter table loan_details add column loan_shown_on_bs tinyint(1) default 0;

# --- !Downs

alter table loan_details drop column amount_per_month_from_sal;
alter table loan_details drop column loan_shown_on_bs;