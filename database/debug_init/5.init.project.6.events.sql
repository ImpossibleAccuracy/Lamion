DO
$do$
    BEGIN
        FOR p_id IN 1..5
            LOOP
                FOR _ IN 1..100000
                    LOOP

                        INSERT INTO "event" (created_at, function_id, user_id, device_id)
                        VALUES (((current_timestamp - INTERVAL '3 MONTH') +
                                 ((current_timestamp + INTERVAL '3 MONTH') - (current_timestamp - INTERVAL '3 MONTH')) *
                                 RANDOM()),
                                (select f.id from project_function f where f.project_id = p_id order by random() limit 1),
                                (select u.id from project_user u where u.project_id = p_id order by random() limit 1),
                                (select d.id from project_device d order by random() limit 1));

                    END LOOP;
            END LOOP;
    END
$do$;
