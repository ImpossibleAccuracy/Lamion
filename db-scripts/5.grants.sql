\connect "Lamion"


REVOKE CONNECT,TEMPORARY ON DATABASE "Lamion" FROM PUBLIC;
REVOKE ALL ON DATABASE "Lamion" FROM root;
GRANT ALL ON DATABASE "Lamion" TO root WITH GRANT OPTION;
GRANT CONNECT,TEMPORARY ON DATABASE "Lamion" TO root;


ALTER TABLE public.application OWNER TO root;
REVOKE ALL ON TABLE public.application FROM root;
GRANT SELECT,INSERT,DELETE,TRIGGER,UPDATE ON TABLE public.application TO root;


ALTER TABLE public.event OWNER TO root;
REVOKE ALL ON TABLE public.event FROM root;
GRANT SELECT,INSERT,DELETE,TRIGGER,UPDATE ON TABLE public.event TO root;


ALTER TABLE public.request OWNER TO root;
REVOKE ALL ON TABLE public.request FROM root;
GRANT SELECT,INSERT,DELETE,TRIGGER,UPDATE ON TABLE public.request TO root;


ALTER TABLE public."user" OWNER TO root;
REVOKE ALL ON TABLE public."user" FROM root;
GRANT SELECT,INSERT,DELETE,TRIGGER,UPDATE ON TABLE public."user" TO root;


ALTER TABLE public.app_analytics OWNER TO root;
REVOKE ALL ON TABLE public.app_analytics FROM root;
GRANT SELECT,TRIGGER ON TABLE public.app_analytics TO root;


ALTER TABLE public.event_analytics OWNER TO root;
REVOKE ALL ON TABLE public.event_analytics FROM root;
GRANT SELECT,TRIGGER ON TABLE public.event_analytics TO root;


ALTER TABLE public.request_analytics OWNER TO root;
REVOKE ALL ON TABLE public.request_analytics FROM root;
GRANT SELECT,TRIGGER ON TABLE public.request_analytics TO root;
