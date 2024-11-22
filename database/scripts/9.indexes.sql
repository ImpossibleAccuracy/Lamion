CREATE INDEX event_function_idx ON event (function_id);
CREATE INDEX event_user_idx ON event (user_id);
CREATE INDEX event_device_idx ON event (device_id);

CREATE INDEX error_user_idx ON error (user_id);
CREATE INDEX error_device_idx ON error (device_id);
CREATE INDEX error_function_idx ON error (function_id);

CREATE INDEX project_function_project_idx ON project_function (project_id);
ALTER TABLE project_function
    ADD UNIQUE (title, project_id);

CREATE INDEX project_user_project_idx ON project_user (project_id);

CREATE INDEX project_device_platform_idx ON project_device (platform_id);

ALTER TABLE device_platform
    ADD UNIQUE (title);
