# --- !Ups

#Adding Relationship With Applicant  
alter table applicant add column relationship_with_applicant varchar(100);

# --- !Downs

alter table applicant drop column relationship_with_applicant;