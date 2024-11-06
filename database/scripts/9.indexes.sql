CREATE INDEX event_function_idx ON event (function_id);
CREATE INDEX event_user_idx ON event (user_id);
CREATE INDEX event_device_idx ON event (device_id);

CREATE INDEX error_user_idx ON error (user_id);
CREATE INDEX error_device_idx ON error (device_id);
CREATE INDEX error_function_idx ON error (function_id);
