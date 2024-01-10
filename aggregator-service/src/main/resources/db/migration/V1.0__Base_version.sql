CREATE TABLE IF NOT EXISTS public.keyword
(
    id SERIAL NOT NULL,
    name varchar(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.source
(
    id SERIAL NOT NULL,
    name varchar(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.article
(
    id SERIAL NOT NULL,
    title varchar(400) NOT NULL,
    description varchar(800),
    content varchar(800),
    url varchar(400),
    date_added date,
    source_id integer,
    is_processed boolean,
    PRIMARY KEY (id),
    CONSTRAINT source_id FOREIGN KEY (source_id)
        REFERENCES public.source (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.article_keyword
(
    article_id integer NOT NULL,
    keyword_id integer NOT NULL,
    FOREIGN KEY (article_id)
        REFERENCES public.article (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    FOREIGN KEY (keyword_id)
        REFERENCES public.keyword (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
