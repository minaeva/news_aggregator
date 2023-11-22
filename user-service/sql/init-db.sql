CREATE TABLE IF NOT EXISTS public.reader
(
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    email character varying(40) NOT NULL,
    password character varying(400),
    role character varying(40),
    registration_source character varying(40),
    times_per_day integer,
    PRIMARY KEY (email)
    );

CREATE TABLE IF NOT EXISTS public.keyword
(
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.reader_keyword
(
    reader_email character varying(40) NOT NULL,
    keyword_id integer NOT NULL,
    FOREIGN KEY (reader_email)
        REFERENCES public.reader (email) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    FOREIGN KEY (keyword_id)
        REFERENCES public.keyword (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);