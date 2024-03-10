--liquibase formatted sql
--changeset Viktor Nakonechnyy:1

create table links
(
    link_id     bigint primary key generated always as identity,
    link_domain varchar(255) not null,
    url         text         not null,

    unique (url)
);

create table chats
(
    id      bigint generated always as identity primary key,
    chat_id bigint not null unique
);

create table chat_link
(
    chat_id bigint not null,
    link_id bigint not null,
    primary key (chat_id, link_id),
    foreign key (chat_id) references chats (chat_id) on delete cascade,
    foreign key (link_id) references links (link_id) on delete cascade
);
