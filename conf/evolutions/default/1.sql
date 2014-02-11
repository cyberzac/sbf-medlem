# --- !Ups
create table member (
    id mediumint not null auto_increment primary key,
    email varchar(255) unique not null,
    name varchar(255),
    address varchar(255),
    zip varchar(255),
    city varchar(255),
    comment varchar(255),
    created_date timestamp not null,
    approved boolean not null
);


# --- !Downs

drop table if exists member;

