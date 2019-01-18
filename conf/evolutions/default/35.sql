# --- !Ups

alter table user add constraint uk_user_api_key unique key(api_key);

# --- !Downs

alter table user drop index uk_user_api_key;