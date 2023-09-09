CREATE USER news_admin WITH SUPERUSER PASSWORD 'news_admin';

CREATE DATABASE aggregator_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

GRANT ALL ON DATABASE aggregator_db TO news_admin;

CREATE TABLE public.category
(
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.source
(
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.article
(
    id integer NOT NULL,
    title character varying(20) NOT NULL,
    description character varying(200),
    url character varying(50),
    date_added date,
    source_id integer,
    PRIMARY KEY (id),
    CONSTRAINT source_id FOREIGN KEY (source_id)
        REFERENCES public.source (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

CREATE TABLE public.article_category
(
    article_id integer NOT NULL,
    category_id integer NOT NULL,
    FOREIGN KEY (article_id)
        REFERENCES public.article (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    FOREIGN KEY (category_id)
        REFERENCES public.category (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);