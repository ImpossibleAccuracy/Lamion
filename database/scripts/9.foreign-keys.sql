ALTER TABLE file
    ADD FOREIGN KEY ("type_id") REFERENCES file_type ("id")
        ON DELETE RESTRICT;

ALTER TABLE account
    ADD FOREIGN KEY ("avatar_id") REFERENCES file ("id")
        ON DELETE SET NULL;

ALTER TABLE role_account_ref
    ADD FOREIGN KEY ("role_id") REFERENCES role ("id")
        ON DELETE CASCADE;

ALTER TABLE role_account_ref
    ADD FOREIGN KEY ("account_id") REFERENCES account ("id")
        ON DELETE CASCADE;

ALTER TABLE project
    ADD FOREIGN KEY ("owner_id") REFERENCES account ("id")
        ON DELETE CASCADE;

ALTER TABLE project_access_key
    ADD FOREIGN KEY ("project_id") REFERENCES project ("id")
        ON DELETE CASCADE;

ALTER TABLE project_device
    ADD FOREIGN KEY ("platform_id") REFERENCES device_platform ("id")
        ON DELETE RESTRICT;

ALTER TABLE project_user
    ADD FOREIGN KEY ("project_id") REFERENCES project ("id")
        ON DELETE CASCADE;

ALTER TABLE project_function
    ADD FOREIGN KEY ("project_id") REFERENCES project ("id")
        ON DELETE CASCADE;

ALTER TABLE function_tag_ref
    ADD FOREIGN KEY ("tag_id") REFERENCES function_tag ("id")
        ON DELETE CASCADE;

ALTER TABLE function_tag_ref
    ADD FOREIGN KEY ("function_id") REFERENCES project_function ("id")
        ON DELETE CASCADE;

ALTER TABLE project_feature
    ADD FOREIGN KEY ("project_id") REFERENCES project ("id")
        ON DELETE CASCADE;

ALTER TABLE function_feature_ref
    ADD FOREIGN KEY ("feature_id") REFERENCES project_feature ("id")
        ON DELETE CASCADE;

ALTER TABLE function_feature_ref
    ADD FOREIGN KEY ("function_id") REFERENCES project_function ("id")
        ON DELETE CASCADE;

ALTER TABLE event
    ADD FOREIGN KEY ("function_id") REFERENCES project_function ("id")
        ON DELETE CASCADE;

ALTER TABLE event
    ADD FOREIGN KEY ("user_id") REFERENCES project_user ("id")
        ON DELETE CASCADE;

ALTER TABLE event
    ADD FOREIGN KEY ("device_id") REFERENCES project_device ("id")
        ON DELETE CASCADE;

ALTER TABLE error
    ADD FOREIGN KEY ("user_id") REFERENCES project_user ("id")
        ON DELETE CASCADE;

ALTER TABLE error
    ADD FOREIGN KEY ("device_id") REFERENCES project_device ("id")
        ON DELETE CASCADE;

ALTER TABLE error
    ADD FOREIGN KEY ("function_id") REFERENCES project_function ("id")
        ON DELETE CASCADE;
