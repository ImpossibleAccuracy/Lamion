ALTER TABLE File ADD FOREIGN KEY ("type_id") REFERENCES FileType ("id");

ALTER TABLE Account ADD FOREIGN KEY ("avatar_id") REFERENCES File ("id");

ALTER TABLE Role_Account ADD FOREIGN KEY ("role_id") REFERENCES Role ("id");

ALTER TABLE Role_Account ADD FOREIGN KEY ("account_id") REFERENCES Account ("id");

ALTER TABLE Project ADD FOREIGN KEY ("owner_id") REFERENCES Account ("id");

ALTER TABLE ProjectAccessKey ADD FOREIGN KEY ("project_id") REFERENCES Project ("id");

ALTER TABLE ProjectDevice ADD FOREIGN KEY ("platform_id") REFERENCES DevicePlatform ("id");

ALTER TABLE ProjectUser ADD FOREIGN KEY ("project_id") REFERENCES Project ("id");

ALTER TABLE ProjectFunction ADD FOREIGN KEY ("project_id") REFERENCES Project ("id");

ALTER TABLE Function_Tag ADD FOREIGN KEY ("tag_id") REFERENCES FunctionTag ("id");

ALTER TABLE Function_Tag ADD FOREIGN KEY ("function_id") REFERENCES ProjectFunction ("id");

ALTER TABLE ProjectFeature ADD FOREIGN KEY ("project_id") REFERENCES Project ("id");

ALTER TABLE Function_Feature ADD FOREIGN KEY ("feature_id") REFERENCES ProjectFeature ("id");

ALTER TABLE Function_Feature ADD FOREIGN KEY ("function_id") REFERENCES ProjectFunction ("id");

ALTER TABLE Event ADD FOREIGN KEY ("function_id") REFERENCES ProjectFunction ("id");

ALTER TABLE Event ADD FOREIGN KEY ("user_id") REFERENCES ProjectUser ("id");

ALTER TABLE Event ADD FOREIGN KEY ("device_id") REFERENCES ProjectDevice ("id");

ALTER TABLE Error ADD FOREIGN KEY ("user_id") REFERENCES ProjectUser ("id");

ALTER TABLE Error ADD FOREIGN KEY ("device_id") REFERENCES ProjectDevice ("id");
