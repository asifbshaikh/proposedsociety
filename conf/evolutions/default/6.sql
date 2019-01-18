# --- !Ups
ALTER TABLE requirement CHANGE type requirement_type varchar(30);

# --- !Downs
ALTER TABLE requirement CHANGE requirement_type type varchar(255);