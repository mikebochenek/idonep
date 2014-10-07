# --- First database schema

# --- !Ups

create table user (
  email                     varchar(255) not null primary key,
  name                      varchar(255) not null,
  password                  varchar(255) not null
);

create table done (
  id                        bigint not null primary key,
  name                      varchar(255) not null,
  folder                    varchar(255) not null
);

create sequence done_seq start with 1000;


# --- !Downs

drop table if exists done;
drop sequence if exists done_seq;
drop table if exists user;
