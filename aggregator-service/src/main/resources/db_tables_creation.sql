docker run --name postgres_docker --restart unless-stopped -e POSTGRES_PASSWORD=superadmin -e POSTGRES_USER=superadmin -p 5432:5432 -d postgres

docker exec -it postgres_docker bash

psql -U superadmin

CREATE USER news_admin WITH SUPERUSER PASSWORD 'news_admin';

CREATE DATABASE aggregator_db
    WITH
    OWNER = news_admin
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

\connect aggregator_db;
SELECT current_database();

DROP TABLE public.article_keyword;
DROP TABLE public.keyword;
DROP TABLE public.article;
DROP TABLE public.source;

CREATE TABLE public.keyword
(
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.source
(
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.article
(
    id integer NOT NULL,
    title character varying(400) NOT NULL,
    description character varying(800),
    content character varying(800),
    url character varying(400),
    date_added date,
    source_id integer,
    is_processed boolean,
    PRIMARY KEY (id),
    CONSTRAINT source_id FOREIGN KEY (source_id)
        REFERENCES public.source (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

CREATE TABLE public.article_keyword
(
    article_id integer NOT NULL,
    keyword_id integer NOT NULL,
    FOREIGN KEY (article_id)
        REFERENCES public.article (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    FOREIGN KEY (keyword_id)
        REFERENCES public.keyword (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);