\connect "Lamion"


CREATE TABLE public.application (
	"id" serial NOT NULL,
	"title" varchar(255) NOT NULL,
	"date" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	"user_id" integer NOT NULL,
	"description" varchar(1024),
	CONSTRAINT "application_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE public.event (
	"id" serial NOT NULL,
	"title" varchar(255) NOT NULL,
	"date" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	"application_id" integer NOT NULL,
	CONSTRAINT "event_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE public.request (
	"id" serial NOT NULL,
	"event_id" bigint NOT NULL,
	"device" varchar(255),
	"date" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT "request_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE public."user" (
	"id" serial NOT NULL,
	"email" varchar(255) NOT NULL UNIQUE,
	"password" varchar(512) NOT NULL,
	CONSTRAINT "user_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);


ALTER TABLE public.application ADD CONSTRAINT "application_fk0" FOREIGN KEY ("user_id") REFERENCES public."user"("id") ON DELETE CASCADE;

ALTER TABLE public.event ADD CONSTRAINT "event_fk0" FOREIGN KEY ("application_id") REFERENCES public.application("id") ON DELETE CASCADE;

ALTER TABLE public.request ADD CONSTRAINT "request_fk0" FOREIGN KEY ("event_id") REFERENCES public.event("id") ON DELETE CASCADE;
