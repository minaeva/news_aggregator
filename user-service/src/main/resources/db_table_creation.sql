docker run --name postgres_docker --restart unless-stopped -e POSTGRES_PASSWORD=superadmin -e POSTGRES_USER=superadmin -p 5432:5432 -d postgres

docker exec -it postgres_docker bash

psql -U superadmin

--CREATE USER news_admin WITH SUPERUSER PASSWORD 'news_admin';

CREATE DATABASE user_db
    WITH
    OWNER = news_admin
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

\connect aggregator_db;
SELECT current_database();

DROP TABLE public.user_entity;

CREATE TABLE public.user_entity
(
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    email character varying(40) NOT NULL,
    password character varying(400),
    role character varying(40),
    registration_source character varying(40),
    PRIMARY KEY (email)
);

