ALTER TABLE public.article DROP COLUMN date_added;
ALTER TABLE public.article ADD COLUMN date_created timestamp;