--liquibase formatted sql
--changeset Viktor Nakonechnyy:1

create table chat_states
(
    chat_state_id bigint generated always as identity,
    chat_id       bigint       not null,
    chat_status   varchar(255) not null
);
