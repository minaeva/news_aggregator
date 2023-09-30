CREATE USER news_admin WITH SUPERUSER PASSWORD 'news_admin';

CREATE DATABASE user_db
    WITH
    OWNER = superadmin
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

GRANT ALL ON DATABASE user_db TO news_admin;

DROP TABLE public.subscription;
DROP TABLE public.user_entity;

CREATE TABLE public.subscription
(
    id integer NOT NULL,
    source character varying(40) NOT NULL,
    category character varying(100) NOT NULL,
    keywords character varying(400) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.user_entity
(
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    email character varying(40) NOT NULL,
    password character varying(400),
    role character varying(40),
    registration_source character varying(40) NOT NULL,
    subscription_id integer,
    PRIMARY KEY (email),
    CONSTRAINT subscription_id FOREIGN KEY (subscription_id)
        REFERENCES public.subscription(id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

