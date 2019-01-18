# --- !Ups

# Scheduler
alter table form add column filled_date datetime ;
alter table form add column completed_date datetime ;

#form - invoice relation
alter table form add column invoice_id bigint ;
alter table form add constraint fk_form_invoice_id foreign key (invoice_id) references invoice(id) on delete set null ;

update form join user, invoice set form.invoice_id = invoice.id where form.id = user.form_id and invoice.user_id = user.id; 

alter table form add column suggested_budget integer;
alter table form add column total_cash integer;
alter table form add column personal_contribution integer;
alter table form add column cash_with_you integer;
alter table form add column calculated_budget integer;
alter table property add column property_desc varchar(80);

# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;

alter table property drop column property_desc;
alter table form drop column calculated_budget;
alter table form drop column cash_with_you; 
alter table form drop column personal_contribution ;
alter table form drop column total_cash;
alter table form drop column suggested_budget;

alter table form DROP FOREIGN KEY fk_form_invoice_id ;
alter table form drop column invoice_id ;

alter table form drop column filled_date ;
alter table form drop column completed_date;

SET FOREIGN_KEY_CHECKS = 1;