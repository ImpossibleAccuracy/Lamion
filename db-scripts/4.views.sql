\connect "Lamion"


CREATE VIEW public.request_analytics AS
 SELECT DISTINCT r.device,
    row_number() OVER () AS id,
    r.event_id,
    count(r.id) AS count
   FROM public.request r
  GROUP BY r.device, r.event_id
  ORDER BY r.event_id, (count(r.id));


CREATE VIEW public.event_analytics AS
 SELECT e.id,
    e.title,
    e.date,
    e.application_id,
    count(r.id) AS requests_count,
    ( SELECT t.device
           FROM ( SELECT DISTINCT r2d2.device,
                    (- count(r2d2.id)) AS d_count
                   FROM public.request r2d2
                  WHERE (r2d2.event_id = e.id)
                  GROUP BY r2d2.device
                  ORDER BY (- count(r2d2.id))
                 LIMIT 1) t) AS most_used_device,
    ( SELECT to_char(to_timestamp(((sum(EXTRACT(epoch FROM r.date)) / (( SELECT count(r.id) AS count))::numeric))::double precision), 'HH24:MI:SS'::text) AS to_char) AS high_demand_time
   FROM (public.event e
     LEFT JOIN public.request r ON ((r.event_id = e.id)))
  GROUP BY e.id
  ORDER BY (- e.id);


CREATE VIEW public.app_analytics AS
 SELECT a.id,
    a.title,
    a.date,
    a.description,
    a.user_id,
    count(e.id) AS events_count,
    COALESCE(sum(e.requests_count), (0)::numeric) AS requests_count
   FROM (public.application a
     LEFT JOIN public.event_analytics e ON ((e.application_id = a.id)))
  GROUP BY a.id
  ORDER BY (- a.id);
