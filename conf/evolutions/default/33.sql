# --- !Ups

insert into page(formatted_title, title, content) values ('concept', 'Our Concept', '');
insert into page(formatted_title, title, content) values ('benefits', 'Benefits of Concept', '');
insert into page(formatted_title, title, content) values ('rules', 'Rules & Regulations', '');
insert into page(formatted_title, title, content) values ('terms_and_conditions', 'Applicants Terms & Conditions', '');
insert into page(formatted_title, title, content) values ('success_story', 'Success Story of Concept', '');
insert into page(formatted_title, title, content) values ('history', 'History', '');

# --- !Downs

delete from page where formatted_title = 'concept';
delete from page where formatted_title = 'benefits';
delete from page where formatted_title = 'rules';
delete from page where formatted_title = 'terms_and_conditions';
delete from page where formatted_title = 'success_story';
delete from page where formatted_title = 'history';

