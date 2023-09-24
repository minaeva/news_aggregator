CREATE DATABASE user_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

GRANT ALL ON DATABASE user_db TO news_admin;

DROP TABLE public.role;
DROP TABLE public.user_entity;

CREATE TABLE public.role
(
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.registration_source
(
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.user_entity
(
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    email character varying(40) NOT NULL,
    password character varying(400),
    role character varying(40),
    registration_source character varying(400),
    PRIMARY KEY (email)
);

