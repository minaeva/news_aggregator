ALTER TABLE public.statistics DROP COLUMN count;
ALTER TABLE public.statistics ADD COLUMN date_created timestamp;
ALTER TABLE public.statistics DROP COLUMN keyword;

