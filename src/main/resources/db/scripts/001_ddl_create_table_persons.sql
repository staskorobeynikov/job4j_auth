create table persons (
    id serial primary key not null,
    login varchar(32) unique,
    password varchar(64)
);