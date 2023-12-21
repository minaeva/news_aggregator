ALTER TABLE public.statistics RENAME TO reader_statistics;

CREATE TABLE IF NOT EXISTS public.keyword_statistics
(
    id SERIAL NOT NULL,
    name varchar(100) NOT NULL,
    date_created timestamp,
    PRIMARY KEY (id)
);
CREATE INDEX index_name ON public.keyword_statistics (name);