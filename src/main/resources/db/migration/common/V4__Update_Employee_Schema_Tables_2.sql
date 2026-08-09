ALTER TABLE employee.profile
    ADD CONSTRAINT fk_profile_created_by_1 FOREIGN KEY (created_by) REFERENCES employee.profile (id);
ALTER TABLE employee.profile
    ADD CONSTRAINT fk_profile_updated_by_1 FOREIGN KEY (updated_by) REFERENCES employee.profile (id);