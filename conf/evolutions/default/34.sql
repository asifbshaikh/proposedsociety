# --- !Ups

alter table user add column api_key varchar(255);

# --- !Downs

alter table user drop column api_key;