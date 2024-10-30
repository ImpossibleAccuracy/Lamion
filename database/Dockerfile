FROM postgres:13.3 AS database-debug

COPY ./database/scripts docker-entrypoint-initdb.d
COPY ./database/debug_init docker-entrypoint-initdb.d

FROM postgres:13.3 AS database-prod

COPY ./database/scripts docker-entrypoint-initdb.d
