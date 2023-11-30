CREATE TABLE IF NOT EXISTS public.subscription
(
    id SERIAL NOT NULL,
    name character varying(100) NOT NULL,
    times_per_day integer,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.reader
(
    id SERIAL NOT NULL,
    name character varying(40) NOT NULL,
    email character varying(40) NOT NULL,
    password character varying(400),
    role character varying(40),
    registration_source character varying(40),
    subscription_id integer,
    PRIMARY KEY (email),
    FOREIGN KEY (subscription_id)
        REFERENCES public.subscription (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.keyword
(
    id SERIAL NOT NULL,
    name character varying(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.subscription_keyword
(
    subscription_id integer NOT NULL,
    keyword_id integer NOT NULL,
    FOREIGN KEY (subscription_id)
        REFERENCES public.subscription (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    FOREIGN KEY (keyword_id)
        REFERENCES public.keyword (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
