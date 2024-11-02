ALTER TABLE file ADD FOREIGN KEY ("type_id") REFERENCES file_type ("id");

ALTER TABLE account ADD FOREIGN KEY ("avatar_id") REFERENCES file ("id");

ALTER TABLE role_account_ref ADD FOREIGN KEY ("role_id") REFERENCES role ("id");

ALTER TABLE role_account_ref ADD FOREIGN KEY ("account_id") REFERENCES account ("id");

ALTER TABLE project ADD FOREIGN KEY ("owner_id") REFERENCES account ("id");

ALTER TABLE project_access_key ADD FOREIGN KEY ("project_id") REFERENCES project ("id");

ALTER TABLE project_device ADD FOREIGN KEY ("platform_id") REFERENCES device_platform ("id");

ALTER TABLE project_user ADD FOREIGN KEY ("project_id") REFERENCES project ("id");

ALTER TABLE project_function ADD FOREIGN KEY ("project_id") REFERENCES project ("id");

ALTER TABLE function_tag_ref ADD FOREIGN KEY ("tag_id") REFERENCES function_tag ("id");

ALTER TABLE function_tag_ref ADD FOREIGN KEY ("function_id") REFERENCES project_function ("id");

ALTER TABLE project_feature ADD FOREIGN KEY ("project_id") REFERENCES project ("id");

ALTER TABLE function_feature_ref ADD FOREIGN KEY ("feature_id") REFERENCES project_feature ("id");

ALTER TABLE function_feature_ref ADD FOREIGN KEY ("function_id") REFERENCES project_function ("id");

ALTER TABLE event ADD FOREIGN KEY ("function_id") REFERENCES project_function ("id");

ALTER TABLE event ADD FOREIGN KEY ("user_id") REFERENCES project_user ("id");

ALTER TABLE event ADD FOREIGN KEY ("device_id") REFERENCES project_device ("id");

ALTER TABLE error ADD FOREIGN KEY ("user_id") REFERENCES project_user ("id");

ALTER TABLE error ADD FOREIGN KEY ("device_id") REFERENCES project_device ("id");
