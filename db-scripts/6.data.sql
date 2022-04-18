\connect "Lamion"


INSERT INTO public."user" (email, password) VALUES ('user@mail.com', '$2a$12$d7OJypWLIaHVRCMjEXYD0u9gVUbc0VnXPB5JhH7A0rEcvQPaKvgIm');


INSERT INTO public.application (title, date, user_id, description) VALUES ('App#1', '2022-04-10 15:50:04.898206', 1, 'Безусловно, перспективное планирование влечет за собой процесс внедрения и модернизации укрепления моральных ценностей. Синтетическое тестирование говорит о возможностях первоочередных требований. Ясность нашей позиции очевидна: выбранный нами инновационный путь требует анализа позиций, занимаемых участниками в отношении поставленных задач!');
INSERT INTO public.application (title, date, user_id, description) VALUES ('App#2', '2022-04-10 15:50:04.898206', 1, 'Идейные соображения высшего порядка, а также убеждённость некоторых оппонентов, в своём классическом представлении, допускает внедрение вывода текущих активов. С другой стороны, укрепление и развитие внутренней структуры, в своём классическом представлении, допускает внедрение приоритизации разума над эмоциями! Противоположная точка зрения подразумевает, что сторонники тоталитаризма в науке, превозмогая сложившуюся непростую экономическую ситуацию, указаны как претенденты на роль ключевых факторов.');


INSERT INTO public.event (title, date, application_id) VALUES ('Login', '2022-04-03 19:01:35.614', 1);
INSERT INTO public.event (title, date, application_id) VALUES ('Registration', '2022-04-03 19:01:35.614', 1);
INSERT INTO public.event (title, date, application_id) VALUES ('Order', '2022-04-03 19:01:35.614', 1);

INSERT INTO public.event (title, date, application_id) VALUES ('Sensor#1', '2022-04-03 19:01:35.614', 2);
INSERT INTO public.event (title, date, application_id) VALUES ('Sensor#2', '2022-04-03 19:01:35.614', 2);
INSERT INTO public.event (title, date, application_id) VALUES ('Shutdown', '2022-04-03 19:01:35.614', 2);
INSERT INTO public.event (title, date, application_id) VALUES ('Reboot', '2022-04-03 19:01:35.614', 2);


INSERT INTO public.request (event_id, device) VALUES (1, 'Android');
INSERT INTO public.request (event_id, device) VALUES (1, 'IOS');
INSERT INTO public.request (event_id, device) VALUES (1, 'IOS');
INSERT INTO public.request (event_id, device) VALUES (2, 'Android');
INSERT INTO public.request (event_id, device) VALUES (2, 'Android');
INSERT INTO public.request (event_id, device) VALUES (2, 'IOS');
INSERT INTO public.request (event_id, device) VALUES (3, 'Android');
INSERT INTO public.request (event_id, device) VALUES (3, 'IOS');
INSERT INTO public.request (event_id, device) VALUES (3, 'IOS');
INSERT INTO public.request (event_id, device) VALUES (3, 'IOS');
INSERT INTO public.request (event_id, device) VALUES (3, 'Android');
INSERT INTO public.request (event_id, device) VALUES (3, 'Android');
INSERT INTO public.request (event_id, device) VALUES (3, 'Android');
INSERT INTO public.request (event_id, device) VALUES (3, 'Android');

INSERT INTO public.request (event_id, device) VALUES (4, 'Arduino');
INSERT INTO public.request (event_id, device) VALUES (4, 'Arduino');
INSERT INTO public.request (event_id, device) VALUES (4, 'ESP86');
INSERT INTO public.request (event_id, device) VALUES (5, 'Arduino');
INSERT INTO public.request (event_id, device) VALUES (5, 'ESP86');
INSERT INTO public.request (event_id, device) VALUES (5, 'ESP86');
INSERT INTO public.request (event_id, device) VALUES (6, 'Arduino');
INSERT INTO public.request (event_id, device) VALUES (6, 'Arduino');
INSERT INTO public.request (event_id, device) VALUES (6, 'ESP86');
INSERT INTO public.request (event_id, device) VALUES (7, 'Arduino');
INSERT INTO public.request (event_id, device) VALUES (7, 'ESP86');
INSERT INTO public.request (event_id, device) VALUES (7, 'ESP86');
INSERT INTO public.request (event_id, device) VALUES (7, 'ESP86');
INSERT INTO public.request (event_id, device) VALUES (7, 'ESP86');
