CREATE TABLE IF NOT EXISTS public.statistics
(
    id SERIAL NOT NULL,
    name varchar(100) NOT NULL,
    email varchar(100) NOT NULL,
    count integer,
    keyword varchar(100) NOT NULL,
    PRIMARY KEY (id)
);