--liquibase formatted sql
--changeset Viktor Nakonechnyy:1

create table chat_states
(
    chat_id       bigint primary key not null,
    chat_status   varchar(255) not null
);
