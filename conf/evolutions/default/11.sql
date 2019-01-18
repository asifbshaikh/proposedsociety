# --- !Ups
ALTER TABLE summary MODIFY i_accpt_abv_cal char not null;

# --- !Downs
ALTER TABLE summary MODIFY i_accpt_abv_cal tinyint(1) default 0;